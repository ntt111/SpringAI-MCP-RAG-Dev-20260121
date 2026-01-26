package com.itzixi.controller;
import com.itzixi.bean.ChatEntity;
import com.itzixi.bean.SearchResult;
import com.itzixi.service.ChatService;
import com.itzixi.service.SearXngService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
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

    @Resource
    private ChatService chatService;

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
    /**
     * 结合大模型优化进行实时检索
     * @param chatEntity
     * @return
     */
    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        chatService.doInternetSearch(chatEntity);
    }
}
