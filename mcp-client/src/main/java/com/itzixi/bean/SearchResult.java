package com.itzixi.bean;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    private String title;
    private String content;
    private String url;
    private double score;
}
