package cn.heap.forum.controller;

import cn.heap.forum.pojo.Comment;
import cn.heap.forum.service.CommentService;
import cn.heap.forum.util.ServerResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@Api("Comment控制类接口文档")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @ApiParam("添加评论")
    public ServerResult<Integer> addComment(@RequestBody Comment comment) {
        int result = commentService.addComment(comment);
        if (result > 0) {
            return ServerResult.success(result);
        } else {
            return ServerResult.error(500, "添加评论失败");
        }
    }

    @DeleteMapping("/{commentId}")
    @ApiParam("删除评论")
    public ServerResult<Integer> deleteComment(@PathVariable Integer commentId) {
        int result = commentService.deleteComment(commentId);
        if (result > 0) {
            return ServerResult.success(result);
        } else {
            return ServerResult.error(500, "删除评论失败");
        }
    }

    @PutMapping
    @ApiParam("修改评论")
    public ServerResult<Integer> updateComment(@RequestBody Comment comment) {
        int result = commentService.updateComment(comment);
        if (result > 0) {
            return ServerResult.success(result);
        } else {
            return ServerResult.error(500, "修改评论失败");
        }
    }

    @GetMapping("/{commentId}")
    @ApiParam("根据ID查询评论")
    public ServerResult<Comment> getCommentById(@PathVariable Integer commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment != null) {
            return ServerResult.success(comment);
        } else {
            return ServerResult.error(404, "评论不存在");
        }
    }

    @GetMapping("/post/{postId}")
    @ApiParam("查询某个帖子下的所有评论")
    public ServerResult<List<Comment>> getCommentsByPostId(@PathVariable Integer postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ServerResult.success(comments);
    }
}