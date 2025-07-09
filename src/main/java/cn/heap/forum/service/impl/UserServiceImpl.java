package cn.heap.forum.service.impl;

import cn.heap.forum.mapper.UserMapper;
import cn.heap.forum.pojo.LoginRequest;
import cn.heap.forum.pojo.RegisterRequest;
import cn.heap.forum.pojo.UpdateUserRequest;
import cn.heap.forum.pojo.User;
import cn.heap.forum.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;



    @Override
    public List<User> selectAll(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public boolean register(RegisterRequest registerRequest) {
        System.out.println("=== 开始注册流程 ===");

        // 1. 参数验证
        if (registerRequest == null ||
                registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty() ||
                registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty() ||
                registerRequest.getConfirmPassword() == null || registerRequest.getConfirmPassword().trim().isEmpty()) {
            System.out.println("❌ 参数验证失败");
            return false;
        }
        System.out.println("✅ 参数验证通过");

        // 2. 检查两次密码是否一致
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            System.out.println("❌ 两次密码不一致");
            return false;
        }
        System.out.println("✅ 密码一致性验证通过");

        // 3. 检查用户名是否已存在
        if (isUsernameExists(registerRequest.getUsername())) {
            System.out.println("❌ 用户名已存在: " + registerRequest.getUsername());
            return false;
        }
        System.out.println("✅ 用户名不存在，可以注册");

        // 4. 创建用户对象
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword()); // 直接存储明文密码
        user.setRoleId(registerRequest.getRoleId() != null ? registerRequest.getRoleId() : 1); // 默认角色ID为1
        user.setRegisterTime(LocalDateTime.now());

        System.out.println("✅ 用户对象创建完成: " + user.getUsername() + ", role_id: " + user.getRoleId());

        // 5. 保存到数据库
        try {
            int result = userMapper.insert(user);
            System.out.println("📝 数据库插入结果: " + result);
            if (result > 0) {
                System.out.println("🎉 注册成功!");
                return true;
            } else {
                System.out.println("❌ 数据库插入失败，返回值: " + result);
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ 数据库操作异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isUsernameExists(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public User login(LoginRequest loginRequest) {
        System.out.println("=== 开始登录流程 ===");

        // 1. 参数验证
        if (loginRequest == null ||
                loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            System.out.println("❌ 登录参数验证失败");
            return null;
        }
        System.out.println("✅ 登录参数验证通过");

        // 2. 根据用户名查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginRequest.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            System.out.println("❌ 用户不存在: " + loginRequest.getUsername());
            return null;
        }
        System.out.println("✅ 用户存在: " + user.getUsername());

        // 3. 验证密码（这里是明文比较）
        if (!loginRequest.getPassword().equals(user.getPassword())) {
            System.out.println("❌ 密码错误");
            return null;
        }
        System.out.println("✅ 密码验证通过");

        System.out.println("🎉 登录成功!");
        return user;
    }

    @Override
    public boolean updateUserInfo(Integer userId, UpdateUserRequest updateRequest) {
        System.out.println("=== 开始更新用户信息 ===");

        try {
            // 1. 参数验证
            if (userId == null || updateRequest == null) {
                System.out.println("❌ 参数验证失败: userId或updateRequest为空");
                return false;
            }

            // 2. 获取当前用户信息
            User currentUser = getUserById(userId);
            if (currentUser == null) {
                System.out.println("❌ 用户不存在: userId=" + userId);
                return false;
            }

            System.out.println("✅ 找到用户: " + currentUser.getUsername());

            // 3. 如果要修改密码，需要验证原密码
            if (updateRequest.getNewPassword() != null && !updateRequest.getNewPassword().trim().isEmpty()) {
                if (updateRequest.getPassword() == null || updateRequest.getPassword().trim().isEmpty()) {
                    System.out.println("❌ 修改密码时必须提供原密码");
                    return false;
                }

                if (!currentUser.getPassword().equals(updateRequest.getPassword())) {
                    System.out.println("❌ 原密码错误");
                    return false;
                }

                if (updateRequest.getNewPassword().length() < 6) {
                    System.out.println("❌ 新密码长度至少6个字符");
                    return false;
                }

                if (!updateRequest.getNewPassword().equals(updateRequest.getConfirmNewPassword())) {
                    System.out.println("❌ 两次输入的新密码不一致");
                    return false;
                }

                currentUser.setPassword(updateRequest.getNewPassword());
                System.out.println("✅ 密码更新准备完成");
            }

            // 4. 更新用户名（如果提供了新的用户名）
            if (updateRequest.getUsername() != null && !updateRequest.getUsername().trim().isEmpty()) {
                String newUsername = updateRequest.getUsername().trim();
                if (!newUsername.equals(currentUser.getUsername())) {
                    // 检查新用户名是否已存在
                    if (isUsernameExists(newUsername)) {
                        System.out.println("❌ 新用户名已存在: " + newUsername);
                        return false;
                    }

                    if (newUsername.length() < 3 || newUsername.length() > 50) {
                        System.out.println("❌ 用户名长度必须在3-50个字符之间");
                        return false;
                    }

                    currentUser.setUsername(newUsername);
                    System.out.println("✅ 用户名更新准备完成: " + newUsername);
                }
            }

            // 5. 更新头像路径
            if (updateRequest.getAvatarPath() != null) {
                currentUser.setAvatarPath(updateRequest.getAvatarPath());
                System.out.println("✅ 头像路径更新准备完成");
            }

            // 6. 执行数据库更新
            int result = userMapper.updateById(currentUser);
            if (result > 0) {
                System.out.println("🎉 用户信息更新成功!");
                return true;
            } else {
                System.out.println("❌ 数据库更新失败");
                return false;
            }

        } catch (Exception e) {
            System.out.println("❌ 更新用户信息异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserById(Integer userId) {
        if (userId == null) {
            return null;
        }

        try {
            return userMapper.selectById(userId);
        } catch (Exception e) {
            System.out.println("❌ 查询用户信息异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
