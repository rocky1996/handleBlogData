package com.acat.handleBlogData.controller.resp;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MediaTypeResp {

    private Integer code;

    private String desc;
}
