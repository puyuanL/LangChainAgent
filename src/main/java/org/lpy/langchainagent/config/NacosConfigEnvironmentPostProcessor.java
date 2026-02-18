package org.lpy.langchainagent.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.Properties;

/**
 * 实现EnvironmentPostProcessor：在Spring环境初始化早期加载Nacos配置
 * 确保langchain4j自动配置能读取到配置并创建Bean
 */
public class NacosConfigEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(NacosConfigEnvironmentPostProcessor.class);

    // Nacos配置参数（与application.yml中一致）
    private static final String NACOS_SERVER_ADDR = "172.19.23.84:8848";
    // aliyun_bailian.yml / deepseek-api.yml / ollama_model.yml / demo-api.yml
    private static final String NACOS_DATA_ID = "aliyun_bailian.yml";
    private static final String NACOS_GROUP = "DEFAULT_GROUP";
    private static final String NACOS_NAMESPACE = ""; // public命名空间填空字符串
    private static final int NACOS_TIMEOUT = 5000;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            // 1. 构建Nacos配置服务参数
            Properties properties = new Properties();
            properties.put("serverAddr", NACOS_SERVER_ADDR);
            properties.put("namespace", NACOS_NAMESPACE);
            properties.put("timeout", NACOS_TIMEOUT);

            // 2. 创建Nacos配置客户端并读取配置
            ConfigService configService = NacosFactory.createConfigService(properties);
            String configContent = configService.getConfig(NACOS_DATA_ID, NACOS_GROUP, NACOS_TIMEOUT);

            if (configContent == null || configContent.isEmpty()) {
                log.error("❌ Nacos配置读取失败：dataId={}, group={} 内容为空", NACOS_DATA_ID, NACOS_GROUP);
                return;
            }
            log.info("✅ 早期加载Nacos配置成功：\n{}", configContent);

            // 3. 解析yml并扁平化配置
            Yaml yaml = new Yaml();
            Map<String, Object> configMap = yaml.load(configContent);
            Map<String, Object> flatMap = flattenMap(configMap, "");

            // 4. 将配置注入Spring环境（优先级最高）
            MutablePropertySources propertySources = environment.getPropertySources();
            propertySources.addFirst(new MapPropertySource("nacosEarlyConfig", flatMap));

        } catch (NacosException e) {
            log.error("❌ 连接Nacos失败：{}", NACOS_SERVER_ADDR, e);
            throw new RuntimeException("Nacos配置早期加载失败", e);
        }
    }

    /**
     * 扁平化嵌套配置（如langchain4j.open-ai.chat-model.base-url）
     */
    private Map<String, Object> flattenMap(Map<String, Object> sourceMap, String prefix) {
        return sourceMap.entrySet().stream()
                .collect(
                        java.util.HashMap::new,
                        (map, entry) -> {
                            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
                            Object value = entry.getValue();
                            if (value instanceof Map) {
                                map.putAll(flattenMap((Map<String, Object>) value, key));
                            } else {
                                map.put(key, value);
                            }
                        },
                        java.util.HashMap::putAll
                );
    }
}
