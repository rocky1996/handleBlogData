package com.acat.handleblogdata.controller;

import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.service.Article;
import com.acat.handleBlogData.service.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/es")
public class EsController {

    @Autowired
    private ArticleRepository articleRepository;

    @PostMapping("/save")
    public RestResult save(@RequestBody Article article) {
        Article a = articleRepository.save(article);
        boolean res = a.getId() != null;
        if (res) {
            return new RestResult(RestEnum.SUCCESS);
        }else {
            return new RestResult(RestEnum.FAILED);
        }
    }

    @GetMapping("/findById/{id}")
    public RestResult findById(@PathVariable String id) {
        Optional<Article> article = articleRepository.findById(id);
        return new RestResult(RestEnum.SUCCESS, article);
    }
}
