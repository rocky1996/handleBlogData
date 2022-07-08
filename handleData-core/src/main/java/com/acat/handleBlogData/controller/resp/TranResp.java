package com.acat.handleBlogData.controller.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranResp {

    /**
     * 翻译后的内容
     */
    private String tranTextValue;
}
