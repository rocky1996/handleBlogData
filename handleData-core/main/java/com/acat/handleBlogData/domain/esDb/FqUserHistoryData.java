package com.acat.handleBlogData.domain.esDb;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "FourceQuareHistory")
public class FqUserHistoryData {
}
