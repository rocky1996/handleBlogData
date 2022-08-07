package com.acat.handleBlogData.controller.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexTargetResp {

    /**
     * 索引名
     */
    private String index;

    /**
     * 索引中文名
     */
    private String indexChineseName;

    /**
     * 治理前
     */
    private String beforeTreatment;

    /**
     * 治理后
     */
    private String afterTreatment;

    /**
     * 治理失败
     */
    private String governanceFailure ;

    /**
     * 入库成功
     */
    private String warehousingSucceeded;

    /**
     * 入库失败
     */
    private String warehousingFailed;

    /**
     * 去重数量
     */
    private String removalQuantity;
}
