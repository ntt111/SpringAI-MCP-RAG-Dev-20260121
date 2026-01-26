package com.itzixi.service.impl;

import cn.hutool.json.JSONUtil;
import com.itzixi.bean.ChatEntity;
import com.itzixi.bean.ChatResponseEntity;
import com.itzixi.bean.SearchResult;
import com.itzixi.enums.SSEMsgType;
import com.itzixi.service.ChatService;
import com.itzixi.service.SearXngService;
import com.itzixi.utils.SSEServer;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);
    private ChatClient chatClient;
    @Resource
    private SearXngService searXngService;
    private String systemPrompt=
            "你是一个非常聪明的人工智能助手，可以帮我解决很多问题，我为你取一个名字，你的名字叫'laBuBu'";
    // Dify 智能体引擎构建平台

    private static final String ragPROMPT = """
                                              基于上下文的知识库内容回答问题：
                                              【上下文】
                                              {context}
                                              
                                              【问题】
                                              {question}
                                              
                                              【输出】
                                              如果没有查到，请回复：不知道。
                                              如果查到，请回复具体的内容。不相关的近似内容不必提到。
                                              """;
    /**
     * 提示词三大类型：
     * 1.system类型，模板定义角色
     * 2.user类型
     * 3.assistant助手类型
     * @param chatClientBuilder
     */
    //构造器注入，自动配置方式（推荐）
    public ChatServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultSystem(systemPrompt)
                .build();
    }

    @Override
    public String chatTest(String prompt) {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return chatClient.prompt(prompt).call().content();
    }

    @Override
    public Flux<ChatResponse> streamResponse(String prompt) {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return chatClient.prompt(prompt).stream().chatResponse();
    }

    @Override
    public Flux<String> streamString(String prompt) {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return chatClient.prompt(prompt).stream().content();
    }

    @Override
    public void doChat(ChatEntity chatEntity) {
        String userId=chatEntity.getCurrentUserName();
        String prompt=chatEntity.getMessage();
        String botMsgId=chatEntity.getBotMsgId();
        Flux<String> stringFlux=chatClient.prompt(prompt).stream().content();
        List<String> chatList= stringFlux.toStream().map(chatResponse->{
            String content = chatResponse;
            SSEServer.sendMsg(SSEMsgType.ADD,userId,content);
            return content;
        }).collect(Collectors.toList());
        String fullContent=chatList.stream().collect(Collectors.joining());
        ChatResponseEntity chatResponseEntity=new ChatResponseEntity(fullContent,botMsgId);
        SSEServer.sendMsg(SSEMsgType.FINISH,userId, JSONUtil.toJsonStr(chatResponseEntity));
    }

    @Override
    public void doChatRagSearch(ChatEntity chatEntity, List<Document> ragContext) {
        String userId=chatEntity.getCurrentUserName();
        String question=chatEntity.getMessage();
        String botMsgId=chatEntity.getBotMsgId();
        //构建上下文
        String context=null;
        if(ragContext != null && ragContext.size()>0){
            context = ragContext.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n"));
        }
        //组装提示词
        Prompt prompt=new Prompt(ragPROMPT
                .replace("{context}",context)
                .replace("{question}",question));
        System.out.println("prompt"+prompt);
        Flux<String> stringFlux=chatClient.prompt(prompt).stream().content();
        List<String> chatList= stringFlux.toStream().map(chatResponse->{
            String content = chatResponse;
            SSEServer.sendMsg(SSEMsgType.ADD,userId,content);
            return content;
        }).collect(Collectors.toList());
        String fullContent=chatList.stream().collect(Collectors.joining());
        ChatResponseEntity chatResponseEntity=new ChatResponseEntity(fullContent,botMsgId);
        SSEServer.sendMsg(SSEMsgType.FINISH,userId, JSONUtil.toJsonStr(chatResponseEntity));

    }
    private static final String searPROMPT = """
                                              你是一个互联网搜索大师，请基于以下互联网返回的结果作为上下文，根据你的理解结合用户的提问，综合并且输出专业的回答：
                                              【上下文】
                                              {context}
                                              
                                              【问题】
                                              {question}
                                              
                                              【输出】
                                              如果没有查到，请回复：不知道。
                                              如果查到，请回复具体的内容。不相关的近似内容不必提到。
                                              """;
    @Override
    public void doInternetSearch(ChatEntity chatEntity) {
        String userId=chatEntity.getCurrentUserName();
        String question=chatEntity.getMessage();
        String botMsgId=chatEntity.getBotMsgId();
        List<SearchResult> list=searXngService.search(question);
        Prompt prompt=new Prompt(buildSearPrompt(question,list));

        System.out.println("prompt"+prompt);
        Flux<String> stringFlux=chatClient.prompt(prompt).stream().content();
        List<String> chatList= stringFlux.toStream().map(chatResponse->{
            String content = chatResponse;
            SSEServer.sendMsg(SSEMsgType.ADD,userId,content);
            return content;
        }).collect(Collectors.toList());
        String fullContent=chatList.stream().collect(Collectors.joining());
        ChatResponseEntity chatResponseEntity=new ChatResponseEntity(fullContent,botMsgId);
        SSEServer.sendMsg(SSEMsgType.FINISH,userId, JSONUtil.toJsonStr(chatResponseEntity));
    }
    private static String buildSearPrompt(String question,List<SearchResult> lists) {
        StringBuilder context=new StringBuilder();
        lists.forEach(list->{
            context.append(String.format("<context>\n[来源] %s \n [摘要] %s \n </context>\n",
                    list.getUrl(),
                    list.getContent()));
        });
         return searPROMPT
                .replace("{context}",context)
                .replace("{question}",question);
    }

}
