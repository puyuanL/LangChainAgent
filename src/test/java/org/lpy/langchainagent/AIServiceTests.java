package org.lpy.langchainagent;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.lpy.langchainagent.assistant.Assistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AIServiceTests {

    @Autowired
    private QwenChatModel qwenChatModel;

    // 手动创建 assistant 类
    @Test
    public void testAIChat() {
        Assistant assistant = AiServices.create(Assistant.class, qwenChatModel);

        String answer = assistant.chat("你好，请介绍一下你自己");
        System.out.println(answer);
    }

    // Auto create assistant
    @Autowired
    private Assistant assistant;
    @Test
    public void testAIChatAuto() {
        String answer = assistant.chat("你好，请介绍一下你自己");
        System.out.println(answer);
    }
}
