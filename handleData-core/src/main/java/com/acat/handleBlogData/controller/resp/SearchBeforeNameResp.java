package com.acat.handleBlogData.controller.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchBeforeNameResp {

    private List<BeforeNameInfo> beforeNameInfoList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BeforeNameInfo {
        /**
         * 数据来源
         */
        private MediaTypeResp mediaTypeResp;

        /**
         * 用户id
         */
        private String userId;

        /**
         * 用户名
         */
        private String userName;

        /**
         * 用户全名
         */
        private String userQuanName;

        /**
         * 用户url
         */
        private String userUrl;
    }
}
