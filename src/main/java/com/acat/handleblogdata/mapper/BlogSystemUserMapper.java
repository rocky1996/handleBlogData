package com.acat.handleblogdata.mapper;

import com.acat.handleblogdata.domain.BlogSystemUser;
import com.acat.handleblogdata.domain.BlogSystemUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlogSystemUserMapper {
    long countByExample(BlogSystemUserExample example);

    int deleteByExample(BlogSystemUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BlogSystemUser record);

    int insertSelective(BlogSystemUser record);

    List<BlogSystemUser> selectByExample(BlogSystemUserExample example);

    BlogSystemUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BlogSystemUser record, @Param("example") BlogSystemUserExample example);

    int updateByExample(@Param("record") BlogSystemUser record, @Param("example") BlogSystemUserExample example);

    int updateByPrimaryKeySelective(BlogSystemUser record);

    int updateByPrimaryKey(BlogSystemUser record);
}