package com.acat.handleBlogData.service.esService.repository.v2;

import com.acat.handleBlogData.domain.esEntityV2.FqUserData_v2;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FqV2Repository extends ElasticsearchRepository<FqUserData_v2, String> {
}
