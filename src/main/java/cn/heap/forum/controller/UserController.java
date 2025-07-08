package cn.heap.forum.controller;

import cn.heap.forum.pojo.User;
import cn.heap.forum.service.UserService;
import cn.heap.forum.util.ServerResult;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "用户接口")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/user/login") // 登录接口,身份判断
    public ServerResult<List<User>> login(){
        return null;
    }

    @RequestMapping("/user/register") // 注册接口
    public ServerResult<List<User>> register(){
        return null;
    }

    @RequestMapping("/user/update")
    public ServerResult<List<User>> update(){
        return null;
    }



}
