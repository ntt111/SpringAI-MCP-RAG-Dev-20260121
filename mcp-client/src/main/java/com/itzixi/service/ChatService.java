package com.itzixi.service;

import com.itzixi.bean.ChatEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import reactor.core.publisher.Flux;

import java.util.List;

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

    /**
     * Rag知识库检索汇总给大模型输出
     * @param
     * @param ragContext
     * @return
     */
    public void doChatRagSearch(ChatEntity chatEntity, List<Document> ragContext);

    /**
     * 基于searXNG实时联网搜索
     * @param
     * @param chatEntity
     * @return
     */
    public void doInternetSearch(ChatEntity chatEntity);

}
