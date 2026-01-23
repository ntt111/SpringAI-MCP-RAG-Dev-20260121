package com.itzixi.controller;
import com.itzixi.bean.SearchResult;
import com.itzixi.service.SearXngService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("internet")
@RequiredArgsConstructor
public class InternetController {

    @Resource
    private SearXngService searXngService;

    private final OkHttpClient client;

    /**
     * 测试Sear
     * @param query
     * @return
     */
    @GetMapping("/test")
    public List<SearchResult> test(@RequestParam("query") String query){
        return searXngService.search(query);
    }
}
