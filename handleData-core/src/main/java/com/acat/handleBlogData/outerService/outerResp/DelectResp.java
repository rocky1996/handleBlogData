package com.acat.handleBlogData.outerService.outerResp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DelectResp {

    private Integer responseStatus;
    private String responseDetails;
    private ResponseData responseData;

    @Data
    public static class ResponseData{
        private Integer confidence;
        private String language;
    }
}
