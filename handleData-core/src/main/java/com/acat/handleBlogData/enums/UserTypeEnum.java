package com.acat.handleBlogData.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public enum UserTypeEnum {

    PERSON(1, "个人账号"),
    GONG_ZHONG(0, "公众账号"),
    WEI_ZHI(-1, "未知"),
            ;
    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private static Map<Integer, UserTypeEnum> enumMap = new HashMap<>();
    static {
        for (UserTypeEnum userTypeEnum : values()) {
            enumMap.put(userTypeEnum.getCode(), userTypeEnum);
        }
    }

    public static UserTypeEnum getUserTypeEnum(Integer code) {
        return enumMap.get(code);
    }
}

