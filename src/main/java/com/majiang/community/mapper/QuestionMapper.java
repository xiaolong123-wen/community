package com.majiang.community.mapper;

import com.majiang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset,
                        @Param(value = "size") Integer size);

    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator=#{id} limit #{offset},#{size}")
    List<Question> listById(@Param("id") Integer  id,
                            @Param("offset") Integer offset,
                            @Param("size") Integer size);

    @Select("select count(1) from question where creator=#{id}")
    Integer countById(@Param("id") Integer id);
}
