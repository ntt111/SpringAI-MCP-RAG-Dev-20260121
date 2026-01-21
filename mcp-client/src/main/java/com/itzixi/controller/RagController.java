package com.itzixi.controller;

import com.itzixi.service.DocumentService;
import com.itzixi.utils.LeeResult;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("rag")
public class RagController {

    @Resource
    private DocumentService documentService;

    @PostMapping("/uploadRagDoc")
    public LeeResult uploadRagDoc(@RequestParam("file") MultipartFile file){
        List<Document>  list=documentService.loadText(file.getResource(), file.getOriginalFilename());
        return LeeResult.ok(list);
    }


}
