package com.acat.handleBlogData.enums;

public enum RestEnum {

    SUCCESS(0, "成功"),
    NICK_NAME_EMPTY(301, "昵称不能为空"),
    FILE_TYPE_ERROR(302, "上传文件类型不正确,请上传txt文件"),
    BATCH_QUERY_FIELD_EMPTY(303, "批量搜索字段不能为空"),
    BATCH_QUERY_FIELD_LIST_EMPTY(304, "上传文件数据为空,请重新上传"),
    FIELD_NOT_SUPPORT_DIM_SEARCH(305, ""),
    USERNAME_EMPTY_PARAM(401, "用户名不能为空"),
    PASSWORD_EMPTY_PARAM(402, "密码不能为空"),
    USER_NOT_EXISTS(403, "该用户不存在"),
    USER_ID_ERROR(405, "用户id不能为空"),
    FEN_YE_ERROR(406, "分页参数错误"),
    MEDIA_SOURCE_ERROR(407, "媒介来源错误"),
    PLEASE_ADD_PARAM(408, "您好,请输入搜索参数,否则结果为空！！！"),
    PARAM_IS_NOT_EMPTY(409, "数据来源或UUID不能为空！！！"),
    TRAN_VALUE_IS_EMPTY(410, "翻译内容不能为空！！！"),
    SERVICE_IS_ERROR(411, "服务报错请重试或联系相关人员！！！"),
//    FORBIDDEN(402, "没有权限"),
//    SYSTEM_ERROR(402, "系统错误"),
//    NO_HAVING_DATA(403, "没有搜索到任何相关数据"),
    FAILED(500, "服务端失败"),
    PLEASE_TRY(504, "请重试"),
    ;

    private Integer code;
    private String msg;

    RestEnum() {
    }

    RestEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
