package com.acat.handleblogdata.controller;

import com.acat.handleblogdata.service.Article;
import com.acat.handleblogdata.service.ArticleRepository;
import com.acat.handleblogdata.constants.RestResult;
import com.acat.handleblogdata.enums.RestEnum;
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
