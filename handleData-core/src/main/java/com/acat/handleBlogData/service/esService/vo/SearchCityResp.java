package com.acat.handleBlogData.service.esService.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SearchCityResp {

    private String cityCode;
    private String cityName;
}
