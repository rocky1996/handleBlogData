package com.acat.handleBlogData.service.esService.repository;

import com.acat.handleBlogData.domain.LinkSchoolUserData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkSchoolRepository extends ElasticsearchRepository<LinkSchoolUserData, String> {
}
