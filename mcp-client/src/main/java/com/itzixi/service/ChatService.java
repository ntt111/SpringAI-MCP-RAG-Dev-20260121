package com.itzixi.service;

import com.itzixi.bean.ChatEntity;
import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

public interface ChatService {
    /**
     * 测试大模型聊天
     * @param prompt
     * @return
     */
    public String chatTest(String prompt);

    /**
     * 流式响应ChatResponse
     * @param prompt
     * @return
     */
    public Flux<ChatResponse> streamResponse(String prompt);

    /**
     * 流式响应String
     * @param prompt
     * @return
     */
    public Flux<String> streamString(String prompt);

    /**
     * 流式响应String-和大模型交互
     * @param chatEntity
     * @return
     */
    public void doChat(ChatEntity chatEntity);
}
