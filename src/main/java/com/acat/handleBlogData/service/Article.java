package com.acat.handleBlogData.service;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "article")
@Data
@Builder
public class Article {

    @Id
    private String id;
    private String title;
    private String content;
    private Integer userId;
    private Date createTime;
}
