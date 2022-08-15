package com.acat.handleBlogData.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public enum BatchSearchFieldEnum {

    screen_name("用户名", "screen_name", false),
    name_userd_before("曾用名", "name_userd_before", false),
    mobile("手机号", "mobile", false),
    email("邮箱", "email", false),
    use_name("用户全名", "use_name", false),
    user_summary("用户简介(仅支持模糊查询)", "user_summary", true),
    works("工作信息(仅支持模糊查询)", "works", true),
    alg_remark("算法标签(仅支持模糊查询)", "alg_remark", true),
    skills_name("技能(仅支持模糊查询)", "skills_name", true),
    educations_school_name("教育经历(仅支持模糊查询)", "educations_school_name", true),
    experiences_company_name("工作经历(仅支持模糊查询)", "experiences_company_name", true),
    ;

    private String fieldName;
    private String fieldValue;
    private boolean isFuzzy;

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public boolean isFuzzy() {
        return isFuzzy;
    }

    public static List<String> mustDimSearchField() {
        return Lists.newArrayList(
                BatchSearchFieldEnum.user_summary.getFieldValue(),
                BatchSearchFieldEnum.alg_remark.getFieldValue(),
                BatchSearchFieldEnum.skills_name.getFieldValue(),
                BatchSearchFieldEnum.educations_school_name.getFieldValue(),
                BatchSearchFieldEnum.experiences_company_name.getFieldValue(),
                BatchSearchFieldEnum.works.getFieldValue()
        );
    }
}
