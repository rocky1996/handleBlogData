package com.acat.handleBlogData.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaSourceEnum {

    TWITTER(0, "TWITTER", "twitter"),
    FB_IMPL(1, "FaceBook重点",  "fb_impl"),
    FB_HISTORY(2, "FaceBook历史", "fb_history"),
    FQ_IMPL(3, "FourceQuare-重点", "fq_impl"),
    FQ_HISTORY(4, "FourceQuare-历史", "fq_history"),
    INSTAGRAM(5, "INSTAGRAM", "instagram"),
    LINKEDIN_IMPL(6, "领英-重点", "link_impl"),
    LINKEDIN_HISTORY(7, "领英-历史", "link_history"),
    LINKEDIN_BUSINESS(8, "领英-企业", "link_business"),
    LINKEDIN_SCHOOL(9, "领英-学校", "link_school"),
    ALL(10, "全部", "all")
    ;

    private Integer code;
    private String desc;
    private String es_index;

    MediaSourceEnum() {}

    MediaSourceEnum(Integer code, String desc, String es_index) {
        this.code = code;
        this.desc = desc;
        this.es_index = es_index;
    }

    public Integer getCode() {
        return code;
    }

//    public void setCode(Integer code) {
//        this.code = code;
//    }

    public String getDesc() {
        return desc;
    }

//    public void setDesc(String desc) {
//        this.desc = desc;
//    }

    public String getEs_index() {
        return es_index;
    }

//    public void setEs_index(String es_index) {
//        this.es_index = es_index;
//    }

    public static Map<Integer, MediaSourceEnum> getEnumMap() {
        return enumMap;
    }

    public static void setEnumMap(Map<Integer, MediaSourceEnum> enumMap) {
        MediaSourceEnum.enumMap = enumMap;
    }

    private static Map<Integer, MediaSourceEnum> enumMap = new HashMap<>();
    private static Map<String, MediaSourceEnum> indexEnumMap = new HashMap<>();
    static {
        for (MediaSourceEnum mediaSourceEnum : values()) {
            enumMap.put(mediaSourceEnum.getCode(), mediaSourceEnum);
            indexEnumMap.put(mediaSourceEnum.getEs_index(), mediaSourceEnum);
        }
    }

    public static MediaSourceEnum getMediaSourceEnum(Integer code) {
        return enumMap.get(code);
    }

    public static MediaSourceEnum getMediaSourceEnumByIndex(String index) {
        return indexEnumMap.get(index);
    }
}
