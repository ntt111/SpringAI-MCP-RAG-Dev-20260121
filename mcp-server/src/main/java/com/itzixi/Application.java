package com.itzixi;

import com.itzixi.mcp.tool.DataTour;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    public ToolCallbackProvider registerMCPTool(DataTour dataTour){
        return MethodToolCallbackProvider.builder()
                .toolObjects(dataTour)
                .build();
    }
}