package com.acat.handleBlogData.controller.req;

import com.acat.handleBlogData.domain.entity.BlogSystemUserEntity;
import com.acat.handleBlogData.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReq {

    private String userName;

    private String passWord;

    private String userNickname;

    /**
     * 组装数据
     * @param userReq
     * @return
     */
    public static BlogSystemUserEntity covertBean(UserReq userReq) {
        BlogSystemUserEntity blogSystemUser = new BlogSystemUserEntity();
        if (StringUtils.isNotBlank(userReq.getUserName())) {
            blogSystemUser.setUsername(userReq.getUserName());
        }
        if (StringUtils.isNotBlank(userReq.getPassWord())) {
            blogSystemUser.setPassword(userReq.getPassWord());
        }
        if (StringUtils.isNotBlank(userReq.getUserNickname())) {
            blogSystemUser.setUserNickname(userReq.getUserNickname());
        }

        Date newDate = new Date();
        blogSystemUser.setIsFlag(StatusEnum.OPEN_EFFECTIVE.getCode());
        blogSystemUser.setCreateTime(newDate);
        blogSystemUser.setUpdateTime(newDate);
        return blogSystemUser;
    }
}
