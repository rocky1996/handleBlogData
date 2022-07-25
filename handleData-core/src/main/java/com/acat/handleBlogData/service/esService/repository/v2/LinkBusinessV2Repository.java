package com.acat.handleBlogData.service.esService.repository.v2;

import com.acat.handleBlogData.domain.esEntityV2.LinkBusinessUserData_v2;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkBusinessV2Repository extends ElasticsearchRepository<LinkBusinessUserData_v2, String> {
}
