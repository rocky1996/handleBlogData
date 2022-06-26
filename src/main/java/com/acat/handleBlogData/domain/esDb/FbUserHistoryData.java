package com.acat.handleBlogData.domain.esDb;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "facebookHistory")
public class FbUserHistoryData {
}
