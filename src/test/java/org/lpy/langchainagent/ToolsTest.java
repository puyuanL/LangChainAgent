package org.lpy.langchainagent;

import org.junit.jupiter.api.Test;
import org.lpy.langchainagent.assistant.SeparateChatAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ToolsTest {

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void testCalculatorTools() {
        //答案：3，689706.4865
        String answer = separateChatAssistant.chatSysMsg(
                8,
                "请计算1+2等于几？475695037565的平方根是多少？"
        );
        System.out.println(answer);
    }

}
