package com.itzixi.service;


import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

import java.util.List;

public interface DocumentService {
    /**
     * 加载文档并读取数据进行保存到知识库
     *
     * @return
     */
    public List<Document> loadText(Resource resource, String fileName);

    /**
     * Rag知识库检索
     * @param question
     * @param ragContext
     * @return
     */
    public List<Document> doSearch(String question);
}
