package com.itzixi.controller;

import com.itzixi.bean.ChatEntity;
import com.itzixi.service.ChatService;
import com.itzixi.service.DocumentService;
import com.itzixi.utils.LeeResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("rag")
public class RagController {

    @Resource
    private DocumentService documentService;

    @Resource
    private ChatService chatService;

    /**
     * 加载文档并读取数据进行保存在知识库
     * @param file
     * @return
     */
    @PostMapping("/uploadRagDoc")
    public LeeResult uploadRagDoc(@RequestParam("file") MultipartFile file){
        List<Document>  list=documentService.loadText(file.getResource(), file.getOriginalFilename());
        return LeeResult.ok(list);
    }

    /**
     * 根据提问从知识库中查询对应知识/资料
     * @param question
     * @return
     */
    @GetMapping("/doSearch")
    public LeeResult doSearch(@RequestParam("question") String question){
        return LeeResult.ok(documentService.doSearch(question));
    }
    /**
     * 根据提问从知识库中查询对应知识/资料
     * @param chatEntity
     * @return
     */
    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response){
        List<Document> ragText=documentService.doSearch(chatEntity.getMessage());
        response.setCharacterEncoding("UTF-8");
        chatService.doChatRagSearch(chatEntity,ragText);
    }

}
