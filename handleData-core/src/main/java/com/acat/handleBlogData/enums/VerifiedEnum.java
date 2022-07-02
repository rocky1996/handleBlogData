package com.acat.handleBlogData.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public enum VerifiedEnum {

    REN_ZHENG(1, "认证"),
    FEI_REN_ZHENG(0, "非认证"),
    ;
    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private static Map<Integer, VerifiedEnum> enumMap = new HashMap<>();
    static {
        for (VerifiedEnum verifiedEnum : values()) {
            enumMap.put(verifiedEnum.getCode(), verifiedEnum);
        }
    }

    public static VerifiedEnum getVerifiedEnum(Integer code) {
        return enumMap.get(code);
    }
}
