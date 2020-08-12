package com.majiang.community.mapper;

import com.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//类和类之间传输用dto,数据库的传输使用model
@Mapper
@Repository
//代表持久层
public interface UserMapper{

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);


}