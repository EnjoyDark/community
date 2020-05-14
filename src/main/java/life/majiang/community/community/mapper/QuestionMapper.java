package life.majiang.community.community.mapper;

import life.majiang.community.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,  gmt_create, gmt_modified, creator, tag, description) values(#{title}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag}, #{description})")
    void create(Question question);
}

