package cn.heap.forum.service;

import cn.heap.forum.pojo.LoginRequest;
import cn.heap.forum.pojo.RegisterRequest;
import cn.heap.forum.pojo.UpdateUserRequest;
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
    
    /**
     * 用户登录
     * @param loginRequest 登录请求信息
     * @return 登录用户信息，登录失败返回null
     */
    User login(LoginRequest loginRequest);
    
    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param updateRequest 更新请求信息
     * @return 是否更新成功
     */
    boolean updateUserInfo(Integer userId, UpdateUserRequest updateRequest);
    
    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Integer userId);
}
