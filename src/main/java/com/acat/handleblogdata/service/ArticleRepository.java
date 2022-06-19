package com.acat.handleblogdata.service;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

}
