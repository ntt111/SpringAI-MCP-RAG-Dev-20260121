package com.itzixi.controller;

import com.itzixi.service.ChatService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("hello")
public class HelloController {

    @Resource
    private ChatService chatService;

    @GetMapping("/world")
    public String hello(){
        return "Hello World,你好";
    }

    @GetMapping("/chat")
    public String chat(String msg){
        return chatService.chatTest(msg);
    }

    @GetMapping("/chat/stream/response")
    public Flux<ChatResponse> chatResponse(String msg,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        return chatService.streamResponse(msg);
    }

    @GetMapping("/chat/stream/string")
    public Flux<String> chatString(String msg,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        return chatService.streamString(msg);
    }
}
