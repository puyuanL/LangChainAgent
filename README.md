## LangChainAgent



### 一、项目介绍

项目采用 LangChain4J + SpringBoot 实现大模型智能体的部署

nacos: http://172.19.23.84:8080/index.html#/login





### 二、数据存储

采用 `MangoDB` ，一种 `NoSQL` 数据库，可以应对大量数据、高性能、灵活性的请求。`MangoDB` 是一种文档型数据库，数据以 `JSON-like` 的文档形式存储，数据结构为 `key-value` 键值对。

选择理由：`MySQL` 为关系型数据库，不适合对大模型对话文件等数据进行存储，且高并发能力较差；`Redis` 是一种基于内存的`NoSQL` 数据库，大模型对话文件会导致数据库占用内存过高。

`MangoDB` 不需要预先定义严格的表结构，适合存储半结构化或非结构化的数据。当聊天记忆中包含多样化的信息，如文本消息、图片、语音等多媒体数据，或者消息格式可能会频繁变化时，`MongoDB` 能很好地适应这种灵活性。



### 三、功能实现

#### 1、文件配置

**（1）application：**采用 `SpringCloudClient` 对大模型 `API key` 等敏感信息和参数进行配置，实现 `EnvironmentPostProcessor`：在 `Spring` 环境初始化早期加载 `Nacos` 配置，确保 `Langchain4j` 自动配置能读取到配置并创建 `Bean` 

**（2）AiService：**使用 `AiService` 配置——大模型种类（`qwenChatModel`）、聊天记忆等



#### 2、ChatMemory

聊天记忆 `chatMemoryProvider` ，创建 `Configuration` 配置类，实现 `chatMemoryProvider` ，自定义重写 `chatMemoryStore` 实现聊天记忆的自定义存储。调用时传入 `MemoryId` 用于区分记忆。



#### 3、Persistence

（1）实现 `ChatMessages` 类，作为保存聊天信息的格式。

（2）实现 `ChatMemoryStore` 抽象类（`MongoChatMemoryStore implements ChatMemoryStore`）——重写 `getMessages`、`updateMessages`、`deleteMessages` 三个方法，实现将对话信息保存到数据库中

（3）将 `MongoChatMemoryStore` 注入 `Config` 类中，将其作为 `chatMemoryStore` 



#### 4、Prompt

采用 @SystemMessage、@UserMessage、@V 三种注解实现系统和多个用户提示词设置



