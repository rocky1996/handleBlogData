package com.acat.handleBlogData.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaSourceEnum {

    TWITTER(0, "Twitter", "twitter", "twitter_v4"),
    FB_IMPL(1, "FaceBook",  "fb_impl", "fb_v4"),
    FB_HISTORY(2, "FaceBook", "fb_history", "fb_v4"),
    FQ_IMPL(3, "Foursquare", "fq_impl", "fq_v2"),
    FQ_HISTORY(4, "Foursquare", "fq_history", "fq_v2"),
    INSTAGRAM(5, "Instagram", "instagram", "instagram_v4"),
    LINKEDIN_IMPL(6, "LinkedIn", "link_impl", "link_v2"),
    LINKEDIN_HISTORY(7, "LinkedIn", "link_history", "link_v2"),
    LINKEDIN_BUSINESS(8, "LinkedIn-企业", "link_business", "link_business_v4"),
    LINKEDIN_SCHOOL(9, "LinkedIn-学校", "link_school", "link_school_v4"),
    ALL(10, "全部", "all", "all"),
    ;

    private Integer code;
    private String desc;
    private String es_index_v1;
    private String es_index_v2;

    MediaSourceEnum() {}

    MediaSourceEnum(Integer code, String desc, String es_index_v1, String es_index_v2) {
        this.code = code;
        this.desc = desc;
        this.es_index_v1 = es_index_v1;
        this.es_index_v2 = es_index_v2;
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

    public String getEs_index_v1() {
        return es_index_v1;
    }

    public String getEs_index_v2() {
        return es_index_v2;
    }

//    public void setEs_index(String es_index) {
//        this.es_index = es_index;
//    }

//    public static Map<Integer, MediaSourceEnum> getEnumMap() {
//        return enumMap;
//    }
//
//    public static void setEnumMap(Map<Integer, MediaSourceEnum> enumMap) {
//        MediaSourceEnum.enumMap = enumMap;
//    }

    private static Map<Integer, MediaSourceEnum> enumMap = new HashMap<>();
    private static Map<String, MediaSourceEnum> indexEnumMap = new HashMap<>();
    private static Map<String, MediaSourceEnum> indexEnumMapV2 = new HashMap<>();
    static {
        for (MediaSourceEnum mediaSourceEnum : values()) {
            enumMap.put(mediaSourceEnum.getCode(), mediaSourceEnum);
            indexEnumMap.put(mediaSourceEnum.getEs_index_v1(), mediaSourceEnum);
            indexEnumMapV2.put(mediaSourceEnum.getEs_index_v2(), mediaSourceEnum);
        }
    }

    public static MediaSourceEnum getMediaSourceEnum(Integer code) {
        return enumMap.get(code);
    }

    public static MediaSourceEnum getMediaSourceEnumByIndex(String index) {
        return indexEnumMap.get(index);
    }

    public static MediaSourceEnum getMediaSourceEnumByIndexV2(String index) {
        return indexEnumMapV2.get(index);
    }

//    public static void main(String[] args) {
//        System.out.println(MediaSourceEnum.TWITTER.getEs_index());
//    }
}
