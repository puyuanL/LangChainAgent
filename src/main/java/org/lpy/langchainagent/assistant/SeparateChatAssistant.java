package org.lpy.langchainagent.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "qwenChatModel",
        chatMemoryProvider = "chatMemoryProvider",
        tools = "calculatorTools"
)
public interface SeparateChatAssistant {
    /**
     * 分离聊天记录
     * SystemMessage    系统提示词  作为系统消息，在对话之前单独发送给大模型。
     * UserMessage      用户提示词  将用户消息添加到提示词的 {{it}} 占位符处，一起发送给大模型
     * @param memoryId 聊天id
     * @param userMessage 用户消息
     * @return String answer
     */
    @SystemMessage(fromResource = "templates/prompt-template.txt")
    String chat(
            @MemoryId int memoryId,
            @UserMessage String userMessage,
            @V("username") String username,
            @V("age") int age
    );

    @SystemMessage("从现在开始，无论你收到的问题是什么，你只被允许英语回答问题。今天的日期是{{current_date}}")
//    @SystemMessage(fromResource = "templates/prompt-template.txt")
    String chatSysMsg(@MemoryId int memoryId, @UserMessage String userMessage);

    /* {{it}}表示这里唯一的参数的占位符
     * 单参数时 无需添加任何参数注解
     * 多参数时 每个参数都需要添加注解
     * 因此 方法级@UserMessage注解 只能通过参数注解@V实现
     * 如 chatVMsg 所示
     */
    @UserMessage("从现在开始，你只被允许英语回答问题，并且添加一些表情符号，今天的日期是{{current_date}}。{{it}}")
    String chatUserMsg(String userMessage);

    @UserMessage("从现在开始，你只被允许英语回答问题，并且添加一些表情符号，今天的日期是{{current_date}}。{{msg}}")
    String chatVMsg(@MemoryId int memoryId, @V("msg") String userMessage);
}
