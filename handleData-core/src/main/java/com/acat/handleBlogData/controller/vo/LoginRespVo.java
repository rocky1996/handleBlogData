package com.acat.handleBlogData.controller.vo;

import com.acat.handleBlogData.domain.mysqlDb.BlogSystemUser;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginRespVo {

    private Integer id;
    private String userName;
    private String passWord;
    private String userNickname;

    public static LoginRespVo covertBean(BlogSystemUser blogSystemUser) {
        if (blogSystemUser != null) {
            return LoginRespVo
                    .builder()
                    .id(blogSystemUser.getId())
                    .userName(blogSystemUser.getUsername())
                    .passWord(blogSystemUser.getPassword())
                    .userNickname(blogSystemUser.getUserNickname())
                    .build();
        }
        return null;
    }
}
