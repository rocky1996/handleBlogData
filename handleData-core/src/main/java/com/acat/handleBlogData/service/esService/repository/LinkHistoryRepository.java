package com.acat.handleBlogData.service.esService.repository;

import com.acat.handleBlogData.domain.LInkUserHistoryData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkHistoryRepository extends ElasticsearchRepository<LInkUserHistoryData, String> {
}
