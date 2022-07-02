package com.acat.handleBlogData.controller.resp;

//import com.acat.handleBlogData.domain.BlogSystemUser;
import com.acat.handleBlogData.domain.entity.BlogSystemUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRespVo {

    private Integer id;
    private String userName;
    private String passWord;
    private String userNickname;

    public static LoginRespVo covertBean(BlogSystemUserEntity blogSystemUser) {
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
