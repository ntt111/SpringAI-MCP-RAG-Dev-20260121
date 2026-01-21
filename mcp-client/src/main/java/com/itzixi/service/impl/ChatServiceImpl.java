package com.itzixi.service.impl;

import cn.hutool.json.JSONUtil;
import com.itzixi.bean.ChatEntity;
import com.itzixi.bean.ChatResponseEntity;
import com.itzixi.enums.SSEMsgType;
import com.itzixi.service.ChatService;
import com.itzixi.utils.SSEServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);
    private ChatClient chatClient;
    private String systemPrompt=
            "你是一个非常聪明的人工智能助手，可以帮我解决很多问题，我为你取一个名字，你的名字叫'laBuBu'";

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
            log.info("userId: {}", userId);
            log.info("content: {}", content);
            return content;
        }).collect(Collectors.toList());
        String fullContent=chatList.stream().collect(Collectors.joining());
        ChatResponseEntity chatResponseEntity=new ChatResponseEntity(fullContent,botMsgId);
        SSEServer.sendMsg(SSEMsgType.FINISH,userId, JSONUtil.toJsonStr(chatResponseEntity));
    }


}
