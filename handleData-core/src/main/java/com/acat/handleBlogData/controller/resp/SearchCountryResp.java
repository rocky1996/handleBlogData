package com.acat.handleBlogData.controller.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCountryResp {

    private String countryCode;
    private String countryName;
}
