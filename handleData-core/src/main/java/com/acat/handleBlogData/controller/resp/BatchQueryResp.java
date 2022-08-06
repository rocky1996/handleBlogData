package com.acat.handleBlogData.controller.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchQueryResp {

    private List<Field> queryFieldList;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Field{
        private String fieldName;
        private String fieldValue;
        private boolean isFuzzy;
    }
}
