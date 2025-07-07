package cn.heap.forum.controller;

import cn.heap.forum.pojo.RegisterRequest;
import cn.heap.forum.pojo.User;
import cn.heap.forum.service.UserService;
import cn.heap.forum.util.ServerResult;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录", notes = "用户身份验证登录")
    @RequestMapping("/user/login") // 登录接口,身份判断
    public ServerResult<List<User>> login(){
        return null;
    }

    @ApiOperation(value = "用户注册", notes = "新用户注册")
    @PostMapping("/user/register") // 注册接口
    public ServerResult<String> register(@RequestBody RegisterRequest registerRequest){
        try {
            // 1. 参数基本验证
            if (registerRequest == null) {
                return ServerResult.error(400, "注册信息不能为空");
            }
            
            if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
                return ServerResult.error(400, "用户名不能为空");
            }
            
            if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
                return ServerResult.error(400, "密码不能为空");
            }
            
            if (registerRequest.getConfirmPassword() == null || registerRequest.getConfirmPassword().trim().isEmpty()) {
                return ServerResult.error(400, "确认密码不能为空");
            }
            
            // 2. 用户名长度验证
            if (registerRequest.getUsername().length() < 3 || registerRequest.getUsername().length() > 50) {
                return ServerResult.error(400, "用户名长度必须在3-50个字符之间");
            }
            
            // 3. 密码强度验证
            if (registerRequest.getPassword().length() < 6) {
                return ServerResult.error(400, "密码长度至少6个字符");
            }
            
            // 4. 两次密码一致性验证
            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                return ServerResult.error(400, "两次输入的密码不一致");
            }
            
            // 5. 检查用户名是否已存在
            if (userService.isUsernameExists(registerRequest.getUsername())) {
                return ServerResult.error(409, "用户名已存在，请选择其他用户名");
            }
            
            // 6. 执行注册
            boolean success = userService.register(registerRequest);
            if (success) {
                return ServerResult.success("注册成功");
            } else {
                return ServerResult.error(500, "注册失败，请稍后重试");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResult.error(500, "服务器内部错误");
        }
    }

    @ApiOperation(value = "更新用户信息", notes = "更新用户个人信息")
    @RequestMapping("/user/update")
    public ServerResult<List<User>> update(){
        return null;
    }
}
