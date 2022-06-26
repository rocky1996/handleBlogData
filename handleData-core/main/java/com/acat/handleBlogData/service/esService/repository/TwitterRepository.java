package com.acat.handleBlogData.service.esService.repository;

import com.acat.handleBlogData.domain.esDb.TwitterUserData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterRepository extends ElasticsearchRepository<TwitterUserData, String> {
}
