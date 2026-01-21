package com.itzixi.controller;

import com.itzixi.bean.ChatEntity;
import com.itzixi.service.ChatService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Resource
    private ChatService chatService;

    @PostMapping("/doChat")
    public void chat(@RequestBody ChatEntity chatEntity){
        chatService.doChat(chatEntity);
    }


}
