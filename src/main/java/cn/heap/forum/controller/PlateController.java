package cn.heap.forum.controller;


import cn.heap.forum.pojo.Plate;
import cn.heap.forum.pojo.Post;
import cn.heap.forum.service.PlateService;
import cn.heap.forum.util.ServerResult;
import cn.heap.forum.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plate")
@Api(tags = "plate-controller")
public class PlateController {
    @Autowired
    private PlateService plateService;

    @GetMapping("/select")
    @ApiParam(value = "获取所有plate")
    public ServerResult<List<Plate>> selectAll() {
        return ServerResult.success(plateService.selectAll());
    }

    @GetMapping("/postsearch")
    @ApiParam(value = "寻找特定plate的post")
    public ServerResult<List<Post>> selectPostById(int plateId) {
        return ServerResult.success(plateService.search(plateId));
    }

    @GetMapping("/searchbyauthor")
    @ApiParam(value = "寻找author的post")
    public ServerResult<List<Post>> selectPostByUser() {
        try {
            // 从ThreadLocal获取当前登录用户信息
            Integer userId = UserContext.getCurrentUserId();

            if (userId == null)
                return ServerResult.error(401, "用户未登录");

            return ServerResult.success(plateService.authorsearch(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResult.error(500, "查找失败");
        }
    }

    @GetMapping("/searchbycomment")
    @ApiParam(value = "寻找特定comment的post")
    public ServerResult<List<Post>> selectPostBycomment(int commentId) {
        try {
            // 从ThreadLocal获取当前登录用户信息
            Integer userId = UserContext.getCurrentUserId();

            if (userId == null)
                return ServerResult.error(401, "用户未登录");

            return ServerResult.success(plateService.commentsearch(commentId, userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResult.error(500, "查找失败"+ e.getMessage());
        }
    }
}
