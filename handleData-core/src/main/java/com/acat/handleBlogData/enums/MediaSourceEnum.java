package com.acat.handleBlogData.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaSourceEnum {

    TWITTER(0, "Twitter", "twitter"),
    FB_IMPL(1, "FaceBook-完整属性",  "fb_impl"),
    FB_HISTORY(2, "FaceBook-部分属性", "fb_history"),
    FQ_IMPL(3, "Foursquare-完整属性", "fq_impl"),
    FQ_HISTORY(4, "Foursquare-部分属性", "fq_history"),
    INSTAGRAM(5, "Instagram", "instagram"),
    LINKEDIN_IMPL(6, "LinkedIn-完整属性", "link_impl"),
    LINKEDIN_HISTORY(7, "LinkedIn-部分属性", "link_history"),
    LINKEDIN_BUSINESS(8, "LinkedIn-企业", "link_business"),
    LINKEDIN_SCHOOL(9, "LinkedIn-学校", "link_school"),
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

//    public static Map<Integer, MediaSourceEnum> getEnumMap() {
//        return enumMap;
//    }
//
//    public static void setEnumMap(Map<Integer, MediaSourceEnum> enumMap) {
//        MediaSourceEnum.enumMap = enumMap;
//    }

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

//    public static void main(String[] args) {
//        System.out.println(MediaSourceEnum.TWITTER.getEs_index());
//    }
}
