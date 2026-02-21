package org.lpy.langchainagent;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.lpy.langchainagent.assistant.Assistant;
import org.lpy.langchainagent.assistant.MemoryChatAssistant;
import org.lpy.langchainagent.assistant.SeparateChatAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class ChatMemoryTests {

    @Autowired
    private Assistant assistant;

    /**
     * without chat memory
     */
    @Test
    public void testChatMemory1() {
        String answer1 = assistant.chat("你好，我是 puyuan");
        System.out.println(answer1);

        String answer2 = assistant.chat("你好，请问我是谁？");
        System.out.println(answer2);
    }

    /**
     * simple implement of chat memory
     */
    @Autowired
    private QwenChatModel qwenChatModel;
    @Test
    public void testChatMemory2() {
        // first chat
        UserMessage userMessage1 = UserMessage.userMessage("你好，我是 puyuan");
        ChatResponse chatResponse1 = qwenChatModel.chat(userMessage1);
        AiMessage aiMessage1 = chatResponse1.aiMessage();
        System.out.println(aiMessage1.text());

        // second chat
        UserMessage userMessage2 = UserMessage.userMessage("你好，你知道我是谁吗？");
        ChatResponse chatResponse2 = qwenChatModel.chat(
                Arrays.asList(userMessage1, aiMessage1, userMessage2)
        );
        AiMessage aiMessage2 = chatResponse2.aiMessage();
        System.out.println(aiMessage2.text());
    }

    /**
     * use ChatMemory
     */
    @Test
    public void testChatMemory3() {
        // create chatMemory
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        // create AIService
        Assistant assistant = AiServices
                .builder(Assistant.class)
                .chatLanguageModel(qwenChatModel)
                .chatMemory(chatMemory)
                .build();

        // chat with memory
        String answer1 = assistant.chat("你好，我是 puyuan");
        System.out.println(answer1);
        String answer2 = assistant.chat("你好，你知道我是谁吗？");
        System.out.println(answer2);
    }

    /**
     * ChatMemory with AiService
     */
    @Autowired
    private MemoryChatAssistant memoryChatAssistant;
    @Test
    public void testChatMemory4() {
        String answer1 = memoryChatAssistant.chat("你好，我是 puyuan");
        System.out.println(answer1);
        String answer2 = memoryChatAssistant.chat("你好，你知道我是谁吗？");
        System.out.println(answer2);
    }

    /**
     * use different memoryId test memory separation
     */
    @Autowired
    private SeparateChatAssistant separateChatAssistant;
    @Test
    public void testSeparateChatMemory() {
        String answer1 = separateChatAssistant.chatSysMsg(1,"你好，我是 puyuan");
        System.out.println("ChatId 1: " + answer1);
        String answer2 = separateChatAssistant.chatSysMsg(1,"你好，你知道我是谁吗？");
        System.out.println("ChatId 1: " + answer2);
        String answer3 = separateChatAssistant.chatSysMsg(2,"你好，你知道我是谁吗？");
        System.out.println("ChatId 2: " + answer3);
    }

}
