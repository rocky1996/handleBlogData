package com.acat.handleBlogData.controller.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDetailReq {

    private Integer mediaCode;

    private String uuid;
}
