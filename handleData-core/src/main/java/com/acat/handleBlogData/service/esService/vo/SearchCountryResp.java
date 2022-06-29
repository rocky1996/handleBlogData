package com.acat.handleBlogData.service.esService.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCountryResp {

    private String countryCode;
    private String countryName;
}
