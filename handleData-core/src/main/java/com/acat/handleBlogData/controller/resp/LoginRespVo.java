package com.acat.handleBlogData.controller.resp;

//import com.acat.handleBlogData.domain.BlogSystemUser;
import com.acat.handleBlogData.domain.entity.BlogSystemUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRespVo {

    private Integer id;
    private String userName;
    private String passWord;
    private String userNickname;
    private Integer isFlag;
    private Date createTime;
    private Date updateTime;
    private String token;

    public static LoginRespVo covertBean(BlogSystemUserEntity blogSystemUser) {
        if (blogSystemUser != null) {
            return LoginRespVo
                    .builder()
                    .id(blogSystemUser.getId())
                    .userName(blogSystemUser.getUsername())
                    .passWord(blogSystemUser.getPassword())
                    .userNickname(blogSystemUser.getUserNickname())
                    .isFlag(blogSystemUser.getIsFlag())
                    .createTime(blogSystemUser.getCreateTime())
                    .updateTime(blogSystemUser.getUpdateTime())
                    .build();
        }
        return null;
    }
}
