package com.itzixi.service.impl;

import cn.hutool.json.JSONUtil;
import com.itzixi.bean.SearXNGResponse;
import com.itzixi.bean.SearchResult;
import com.itzixi.service.SearXngService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class SearXngServiceImpl implements SearXngService {

    private final OkHttpClient okHttpClient ;

    @Value("${internet.websearch.searxng.url}")
    private String SEARXNG_URL;

    @Value("${internet.websearch.searxng.counts}")
    private Integer COUNTS;



    @Override
    public List<SearchResult> search(String query) {
        //构建url
        HttpUrl url = HttpUrl.get(SEARXNG_URL)
                .newBuilder()
                .addQueryParameter("q",query)
                .addQueryParameter("format","json")
                .build();
        log.info("搜索地址为Searching for url {}", url);
        //构建request
        Request request=new Request.Builder()
                .url(url)
                .build();
        //发送请求
        try (Response response=okHttpClient.newCall(request).execute()){
            //判断请求是否成功还是失败
            if(!response.isSuccessful()){
                throw new RuntimeException("请求失败：http"+response.code());
            }
            //获得响应的数据
            if(response.body()!=null){
                String body=response.body().string();
                return JSONUtil.toBean(body, SearXNGResponse.class).getResults();
            }
            log.error("搜索失败：{}",response.message());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Collections.emptyList();
    }
}
