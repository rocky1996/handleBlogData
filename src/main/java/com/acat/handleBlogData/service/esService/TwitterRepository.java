package com.acat.handleBlogData.service.esService;

import com.acat.handleBlogData.domain.esDb.TwitterUserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterRepository extends ElasticsearchRepository<TwitterUserData, String> {
}
