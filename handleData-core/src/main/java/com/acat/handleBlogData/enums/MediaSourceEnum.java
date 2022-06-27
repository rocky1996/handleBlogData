package com.acat.handleBlogData.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaSourceEnum {

    TWITTER(0),
    FB_IMPL(1),
    FB_HISTORY(2),
    FQ_IMPL(3),
    FQ_HISTORY(4),
    INSTAGRAM(5),
    LINKEDIN_IMPL(6),
    LINKEDIN_HISTORY(7),
    LINKEDIN_BUSINESS(8),
    LINKEDIN_SCHOOL(9),
    ;

    private Integer code;

    MediaSourceEnum() {}

    MediaSourceEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    private static Map<Integer, MediaSourceEnum> enumMap = new HashMap<>();
    static {
        for (MediaSourceEnum mediaSourceEnum : values()) {
            enumMap.put(mediaSourceEnum.getCode(), mediaSourceEnum);
        }
    }

    public static MediaSourceEnum getMediaSourceEnum(Integer code) {
        return enumMap.get(code);
    }
}
