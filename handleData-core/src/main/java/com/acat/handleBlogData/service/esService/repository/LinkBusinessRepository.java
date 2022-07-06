package com.acat.handleBlogData.service.esService.repository;

import com.acat.handleBlogData.domain.LinkBusinessUserData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkBusinessRepository extends ElasticsearchRepository<LinkBusinessUserData, String> {
}
