package com.itzixi.controller;

import com.itzixi.enums.SSEMsgType;
import com.itzixi.service.ChatService;
import com.itzixi.utils.SSEServer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("sse")
public class SSEController {

    @Resource
    private ChatService chatService;

    @GetMapping(path = "/connect",produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter connect(@RequestParam String userId){
        return SSEServer.connect(userId);
    }

    /**
     * 单个SSE消息发送
     * @param userId
     * @param msg
     * @return
     */
    @GetMapping( "/sendMsg")
    public Object sendMsg(@RequestParam String userId, @RequestParam String msg){
        SSEServer.sendMsg(SSEMsgType.MESSAGE,userId,msg);
        return "ok";
    }
    /**
     * 群组SSE消息发送
     * @param
     * @param msg
     * @return
     */
    @GetMapping( "/sendMsgAll")
    public Object sendMsgAll(@RequestParam String msg){
        SSEServer.sendMsgAll(msg);
        return "ok";
    }

    /**
     * 单个SSE消息发送-Add
     * @param
     * @param msg
     * @return
     */
    @GetMapping( "/sendMsgAdd")
    public Object sendMsgAdd(@RequestParam String userId, @RequestParam String msg) throws InterruptedException {
        for (int i=0;i<10;i++){
            Thread.sleep(200L);
            SSEServer.sendMsg(SSEMsgType.ADD,userId,msg);
        }

        return "ok";
    }

}
