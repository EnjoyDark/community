package life.majiang.community.community.mapper;

import life.majiang.community.community.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
}