package com.acat.handleblogdata.controller.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginRespVo {

    private Integer id;
    private String userName;
    private String passWord;
    private String userNickname;
}
