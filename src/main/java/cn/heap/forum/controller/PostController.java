package cn.heap.forum.controller;

import cn.heap.forum.pojo.Post;
import cn.heap.forum.pojo.PostDTO;
import cn.heap.forum.service.PostService;
import cn.heap.forum.util.ServerResult;
import cn.heap.forum.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@Api("Post控制类接口文档")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/select")
    @ApiParam("寻找plateId对应的post")
    public ServerResult<List<Post>> selectAll(int id){
        return ServerResult.success(postService.selectAll(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "创建帖子", notes = "用户创建新帖子")
    public ServerResult<Void> add(@RequestBody PostDTO post) {
        try {
            System.out.println("=== 开始创建帖子 ===");

            // 从ThreadLocal获取当前登录用户信息
            Integer currentUserId = UserContext.getCurrentUserId();

            if (currentUserId == null) {
                System.out.println("❌ ThreadLocal中没有用户信息，用户未登录");
                return ServerResult.error(401, "用户未登录");
            }

            // 设置帖子的创建者信息
            post.setAuthorId(currentUserId);

            postService.add(post);
            return ServerResult.success();

        } catch (Exception e) {
            System.out.println("❌ 创建帖子异常: " + e.getMessage());
            e.printStackTrace();
            return ServerResult.error(500, "创建帖子失败");
        }
    }
}
