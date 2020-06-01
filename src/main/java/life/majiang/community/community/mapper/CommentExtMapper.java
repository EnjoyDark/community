package life.majiang.community.community.mapper;

import life.majiang.community.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}