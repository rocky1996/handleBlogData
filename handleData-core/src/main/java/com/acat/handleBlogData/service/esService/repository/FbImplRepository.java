package com.acat.handleBlogData.service.esService.repository;

import com.acat.handleBlogData.domain.FbUserImplData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FbImplRepository extends ElasticsearchRepository<FbUserImplData, String> {
}