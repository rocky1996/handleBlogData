package com.acat.handleBlogData.service.esService.repository;

import com.acat.handleBlogData.domain.esDb.InstagramUserData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramRepository extends ElasticsearchRepository<InstagramUserData, String> {
}