package com.acat.handleBlogData.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public enum BatchSearchFieldEnum {

    screen_name("用户名", "screen_name"),
    name_userd_before("曾用名", "name_userd_before"),
    mobile("手机号", "mobile"),
    user_summary("用户简介(仅支持模糊查询)", "user_summary"),
    email("邮箱", "email"),
    alg_remark("算法标签(仅支持模糊查询)", "alg_remark"),
    skills_name("技能(仅支持模糊查询)", "skills_name"),
    educations_school_name("教育经历(仅支持模糊查询)", "educations_school_name"),
    experiences_company_name("工作经历(仅支持模糊查询)", "experiences_company_name"),
    ;

    private String fieldName;
    private String fieldValue;

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public static List<String> mustDimSearchField() {
        return Lists.newArrayList(
                BatchSearchFieldEnum.user_summary.getFieldValue(),
                BatchSearchFieldEnum.alg_remark.getFieldValue(),
                BatchSearchFieldEnum.skills_name.getFieldValue(),
                BatchSearchFieldEnum.educations_school_name.getFieldValue(),
                BatchSearchFieldEnum.experiences_company_name.getFieldValue()
                );
    }
}
