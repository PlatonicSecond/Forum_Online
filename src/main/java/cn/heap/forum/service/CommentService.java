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
    // 根据 ID 查询评论
    Comment getCommentById(Integer commentId);
    // 查询某个帖子下的所有评论
    List<Comment> getCommentsByPostId(Integer postId);
    //回复评论
    Comment replyComment(Comment comment);
}