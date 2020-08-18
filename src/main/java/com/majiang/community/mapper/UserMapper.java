package com.majiang.community.mapper;

import com.majiang.community.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

//类和类之间传输用dto,数据库的传输使用model
@Mapper
@Repository
//代表持久层
public interface UserMapper{
//首先是数据库中对应的字段名，后面的实体类中实际的名称
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer  id);

    @Select("select * from user where account_id = #{accountId}")
    User findByaccountId(String accountId);

    @Update("update user name=#{name},account_id=#{accountId},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl}")
    void update(User user);
}