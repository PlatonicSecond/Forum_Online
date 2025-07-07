package cn.heap.forum.service.impl;

import cn.heap.forum.mapper.CommentMapper;
import cn.heap.forum.pojo.Comment;
import cn.heap.forum.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int addComment(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public int deleteComment(Integer id) {
        return commentMapper.deleteById(id);
    }

    @Override
    public int updateComment(Comment comment) {
        return commentMapper.updateById(comment);
    }

    @Override
    public Comment getCommentById(Integer id) {
        return commentMapper.selectById(id);
    }

    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        // 这里可以根据实际需求编写自定义的 SQL 查询语句
        // 假设 Comment 类中有 postId 字段，可以通过 MyBatis-Plus 的条件构造器来查询
        return commentMapper.selectList(new QueryWrapper<Comment>().eq("post_id", postId));
    }
}