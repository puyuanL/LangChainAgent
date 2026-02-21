package org.lpy.langchainagent;

import org.junit.jupiter.api.Test;
import org.lpy.langchainagent.assistant.SeparateChatAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PromptTest {

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void testSystemMessage() {
        String answer = separateChatAssistant.chatSysMsg(3,"我是puyuan，今天的日期是什么？");
        System.out.println(answer);
    }

    @Test
    public void testUserMessage() {
        String answer = separateChatAssistant.chatUserMsg("我是puyuan，今天的日期是什么？");
        System.out.println(answer);
    }

    @Test
    public void testValueMessage() {
        String answer = separateChatAssistant.chatVMsg(5,"我是puyuan，今天的日期是什么？");
        System.out.println(answer);
    }

    @Test
    public void testMultiValueMessage() {
        String answer = separateChatAssistant.chat(6, "我是谁，我多大了", "puyuan", 18);
        System.out.println(answer);
    }

}
