package com.itzixi.service.impl;

import com.itzixi.service.DocumentService;
import com.itzixi.utils.CustomTextSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;;import java.util.List;
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final RedisVectorStore vectorStore;

    /**
     * 加载文档并保存到知识库
     * @param resource
     * @param fileName
     * @return
     */
    @Override
    public List<Document> loadText(Resource resource, String fileName) {
        //加载读取文档
        TextReader reader = new TextReader(resource);
        reader.getCustomMetadata().put("fileName", fileName);
        List<Document> documentList= reader.get();
//        System.out.println("documentList"+documentList);
          //默认文本切分器
//        TokenTextSplitter splitter = new TokenTextSplitter();
//        List<Document> list=splitter.apply(documentList);
        CustomTextSplitter splitter = new CustomTextSplitter();
        List<Document> list=splitter.split(documentList);
        System.out.println("list"+list);
        vectorStore.add(list);
        return documentList;
    }

    @Override
    public List<Document> doSearch(String question) {
        return vectorStore.similaritySearch(question);
    }


}
