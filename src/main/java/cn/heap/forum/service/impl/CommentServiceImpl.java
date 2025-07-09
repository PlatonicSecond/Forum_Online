package cn.heap.forum.service.impl;

import cn.heap.forum.mapper.CommentMapper;
import cn.heap.forum.pojo.Comment;
import cn.heap.forum.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.heap.forum.pojo.User;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 添加评论
     *
     * @param comment 评论实体
     * @return 插入成功的记录数
     */
    @Override
    public int addComment(Comment comment) {
        try {
            return commentMapper.insert(comment);
        } catch (Exception e) {
            logger.error("添加评论时出现异常", e);
            return 0;
        }
    }

    /**
     * 删除评论
     *
     * @param id 评论的ID
     * @return 删除成功的记录数
     */
    @Override
    public int deleteComment(Integer id) {
        try {
            return commentMapper.deleteById(id);
        } catch (Exception e) {
            logger.error("删除评论时出现异常，评论ID: {}", id, e);
            return 0;
        }
    }

    /**
     * 修改评论
     *
     * @param comment 评论实体
     * @return 更新成功的记录数
     */
    @Override
    public int updateComment(Comment comment) {
        try {
            return commentMapper.updateById(comment);
        } catch (Exception e) {
            logger.error("修改评论时出现异常，评论ID: {}", comment.getCommentId(), e);
            return 0;
        }
    }

    /**
     * 根据ID查询评论
     *
     * @param id 评论的ID
     * @return 评论实体
     */
    @Override
    public Comment getCommentById(Integer id) {
        try {
            return commentMapper.selectById(id);
        } catch (Exception e) {
            logger.error("根据ID查询评论时出现异常，评论ID: {}", id, e);
            return null;
        }
    }

    /**
     * 查询某个帖子下的所有评论
     *
     * @param postId 帖子的ID
     * @return 评论列表
     */
    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        try {
            // 这里可以根据实际需求编写自定义的 SQL 查询语句
            // 假设 Comment 类中有 postId 字段，可以通过 MyBatis-Plus 的条件构造器来查询
            return commentMapper.selectList(new QueryWrapper<Comment>().eq("post_id", postId));
        } catch (Exception e) {
            logger.error("查询某个帖子下的所有评论时出现异常，帖子ID: {}", postId, e);
            return null;
        }
    }
    /**
     * 实现用户回复评论的功能
     *
     * @param comment 帖子中用户Id
     * @return 评论列表
     */
    @Override
    public Comment replyComment(Comment comment) {
        try {
            // 获取被回复的原评论
            Comment originalComment = commentMapper.selectById(comment.getCommentId());
            if (originalComment == null) {
                return null; // 原评论不存在
            }

            // 设置回复评论的属性
            comment.setUserId(originalComment.getAuthorId()); // 设置回复目标用户ID
            comment.setCreateTime(new Date()); // 设置创建时间

            // 插入新评论
            int result = commentMapper.insert(comment);

            if (result > 0) {
                // 返回插入成功的评论
                return comment;
            }
            return null;
        } catch (Exception e) {
            // 实际项目中应该记录日志
            e.printStackTrace();
            return null;
        }
    }
}