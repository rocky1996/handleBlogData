package com.acat.handleBlogData.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public enum GenderEnum {

    MAN(-1, "男"),
    WOMAN(1, "女"),
    WEI_ZHI(0, "未知"),
    ;
    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private static Map<Integer, GenderEnum> enumMap = new HashMap<>();
    static {
        for (GenderEnum genderEnum : values()) {
            enumMap.put(genderEnum.getCode(), genderEnum);
        }
    }

    public static GenderEnum getGenderEnum(Integer code) {
        return enumMap.get(code);
    }
}
