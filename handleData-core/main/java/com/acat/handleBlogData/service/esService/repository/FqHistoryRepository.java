package com.acat.handleBlogData.service.esService.repository;

import com.acat.handleBlogData.domain.esDb.FqUserHistoryData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FqHistoryRepository extends ElasticsearchRepository<FqUserHistoryData, String> {
}
