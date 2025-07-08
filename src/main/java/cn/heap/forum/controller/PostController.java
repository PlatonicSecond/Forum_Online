package cn.heap.forum.controller;

import cn.heap.forum.pojo.Post;
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

    @PostMapping("/create")
    @ApiOperation(value = "创建帖子", notes = "用户创建新帖子")
    public ServerResult<String> createPost(@RequestBody Post post) {
        try {
            System.out.println("=== 开始创建帖子 ===");

            // 从ThreadLocal获取当前登录用户信息
            Integer currentUserId = UserContext.getCurrentUserId();
            String currentUsername = UserContext.getCurrentUsername();
            Integer currentRoleId = UserContext.getCurrentUserRoleId();

            System.out.println("🔍 ThreadLocal用户信息检查:");
            System.out.println("  - 用户ID: " + currentUserId);
            System.out.println("  - 用户名: " + currentUsername);
            System.out.println("  - 角色ID: " + currentRoleId);
            System.out.println("  - 是否已登录: " + UserContext.isLoggedIn());

            if (currentUserId == null) {
                System.out.println("❌ ThreadLocal中没有用户信息，用户未登录");
                return ServerResult.error(401, "用户未登录");
            }

            // 设置帖子的创建者信息
            post.setAuthorId(currentUserId);

            System.out.println("✅ ThreadLocal验证成功！");
            System.out.println("📝 帖子信息:");
            System.out.println("  - 内容: " + post.getContent());
            System.out.println("  - 创建者ID: " + post.getAuthorId());
            System.out.println("  - 板块ID: " + post.getPlateId());

            return ServerResult.success("帖子创建成功！ThreadLocal正常工作，用户: " + currentUsername);

        } catch (Exception e) {
            System.out.println("❌ 创建帖子异常: " + e.getMessage());
            e.printStackTrace();
            return ServerResult.error(500, "创建帖子失败");
        }
    }
}
