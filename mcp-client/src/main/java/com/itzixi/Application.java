package com.itzixi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // 加载.env文件
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        //把.env中的配置参数设置到环境变量中
        // 打印验证.env变量
        System.out.println("OPENAI_API_KEY: " + dotenv.get("OPENAI_API_KEY"));
        System.out.println("OPENAI_BASE_URL: " + dotenv.get("OPENAI_BASE_URL"));
        System.out.println("OPENAI_BASE_MODEL: " + dotenv.get("OPENAI_BASE_MODEL"));
        dotenv.entries().forEach(entry->System.setProperty(entry.getKey(),entry.getValue()));
        dotenv.entries().forEach(entry->System.out.println("getKey:"+entry.getKey()+"getValue:"+entry.getValue()));

        System.setProperty("huggingface.hub.cache-dir", "C:/SpringAi-transformer-cache"); // 自定义缓存目录
        System.setProperty("huggingface.hub.proxy.host", "hf-mirror.com"); // 国内镜像

        SpringApplication.run(Application.class, args);
    }
}