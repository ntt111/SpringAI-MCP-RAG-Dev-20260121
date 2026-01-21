package com.itzixi.utils;

import com.itzixi.enums.SSEMsgType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
/*
建立连接
 */
@Slf4j
public class SSEServer {
    private static final Map<String,SseEmitter> sseClients = new ConcurrentHashMap<>();
    public static SseEmitter connect(String userId){
        //设置超时时间，0L表示永不过期，默认30s,超时会抛出异常
        SseEmitter sseEmitter = new SseEmitter(0L);
        //注册回调方法
        sseEmitter.onTimeout(timeoutCallback(userId));
        sseEmitter.onCompletion(onCompletionCallback(userId));
        sseEmitter.onError(onErrorCallback(userId));
        sseClients.put(userId,sseEmitter);
        log.info("SSE连接创建成功,用户Id为{} ",userId);
        return sseEmitter;
    }
    public static void sendMsg(SSEMsgType sseMsgType, String userId, String msg){
        if(CollectionUtils.isEmpty(sseClients)){
            return;
        }
        if(sseClients.containsKey(userId)){
            SseEmitter sseEmitter = sseClients.get(userId);
            sendEmitterMsg(sseEmitter,userId,msg,sseMsgType);
        }

    }
    public static void sendMsgAll(String msg) {
        if (CollectionUtils.isEmpty(sseClients)) {
            return;
        }
        sseClients.forEach((userId,sseEmitter) -> {
            sendEmitterMsg(sseEmitter,userId,msg,SSEMsgType.MESSAGE);
        });
    }

    /**
     * 发送消息类型
     * @param sseEmitter
     * @param userId
     * @param message
     * @param msgType
     */
    private static void sendEmitterMsg(SseEmitter sseEmitter,
                                      String userId,
                                      String message,
                                      SSEMsgType msgType)  {
        SseEmitter.SseEventBuilder event=SseEmitter.event()
                .id(userId)
                .data(message)
                .name(msgType.type);
        System.out.println("123456789========");
        try {
            sseEmitter.send(event);
        } catch (IOException e) {
            log.error("SSE异常，{}",e.getMessage());
            remove(userId);
        }
    }

    public static Runnable timeoutCallback(String userId){
        return () -> {
            log.info("SSE timeout");
            //移除用户方法
            remove(userId);
        };
    }
    public static void remove(String userId){
        //删除用户
        sseClients.remove(userId);
        log.info("SSE client removed,移除的用户Id为{} ",userId);
    }
    public static Runnable onCompletionCallback(String userId){
        return () -> {
            log.info("SSE完成");
            //移除用户方法
            remove(userId);
        };
    }
    public static Consumer<Throwable> onErrorCallback(String userId){
        return (Throwable e)-> {
            log.info("SSE异常");
            //移除用户方法
            remove(userId);
        };
    }


}
