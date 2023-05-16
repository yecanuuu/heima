package com.zhouyu.mapper;

import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("select 'user'")
    public String getUserName();
}
