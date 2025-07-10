package cn.heap.forum.service;

import cn.heap.forum.pojo.Comment;
import java.util.List;

public interface CommentService {
    // 添加评论
    int addComment(Comment comment);
    // 删除评论
    int deleteComment(Integer commentId);
    // 修改评论
    int updateComment(Comment comment);
    // 查询某个帖子下的所有评论
    List<Comment> getCommentsByPostId(Integer postId);

    Comment getCommentById(Integer authorId);
}