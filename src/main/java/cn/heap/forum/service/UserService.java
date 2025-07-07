package cn.heap.forum.service;

import cn.heap.forum.pojo.RegisterRequest;
import cn.heap.forum.pojo.User;

import java.util.List;

public interface UserService {
    List<User> selectAll(Integer id);
    
    /**
     * 用户注册
     * @param registerRequest 注册请求信息
     * @return 是否注册成功
     */
    boolean register(RegisterRequest registerRequest);
    
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean isUsernameExists(String username);
}
