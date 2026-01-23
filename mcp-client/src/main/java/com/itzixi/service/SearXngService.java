package com.itzixi.service;


import com.itzixi.bean.SearchResult;
import java.util.List;

public interface SearXngService {


    /**
     * 调用本地Sear检索
     * @param question
     * @param question
     * @return
     */
    public List<SearchResult> search(String question);
}
