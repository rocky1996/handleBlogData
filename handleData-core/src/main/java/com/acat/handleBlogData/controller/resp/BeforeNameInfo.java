package com.acat.handleBlogData.controller.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeforeNameInfo {

    private String uuid;

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
