package life.majiang.community.community.mapper;

import life.majiang.community.community.dto.QuestionDTO;
import life.majiang.community.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,  gmt_create, gmt_modified, creator, tag, description) values(#{title}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag}, #{description})")
    void create(Question question);

    @Select("select * from question limit #{offset}, #{size}")
    ArrayList<Question> list(@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(*) from question")
    Integer count();

    @Select("select * from question where creator=#{userId} limit #{offset}, #{size}")
    ArrayList<Question> listByUserId(@Param("userId")Integer userId, @Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(*) from question where creator=#{userId}")
    Integer countByUserId(@Param("userId")Integer userId);

    @Select("select * from question where id=#{id}")
    Question getById(@Param("id")Integer id);
}

