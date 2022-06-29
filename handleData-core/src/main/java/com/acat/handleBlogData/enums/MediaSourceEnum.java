package com.acat.handleBlogData.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaSourceEnum {

    TWITTER(0, "TWITTER"),
    FB_IMPL(1, "FaceBook重点"),
    FB_HISTORY(2, "FaceBook历史"),
    FQ_IMPL(3, "FourceQuare-重点"),
    FQ_HISTORY(4, "FourceQuare-历史"),
    INSTAGRAM(5, "INSTAGRAM"),
    LINKEDIN_IMPL(6, "领英-重点"),
    LINKEDIN_HISTORY(7, "领英-历史"),
    LINKEDIN_BUSINESS(8, "领英-企业"),
    LINKEDIN_SCHOOL(9, "领英-学校"),
    ALL(10, "全部")
    ;

    private Integer code;
    private String desc;

    MediaSourceEnum() {}

    MediaSourceEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
