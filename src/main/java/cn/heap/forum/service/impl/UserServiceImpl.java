package cn.heap.forum.service.impl;

import cn.heap.forum.mapper.UserMapper;
import cn.heap.forum.pojo.RegisterRequest;
import cn.heap.forum.pojo.User;
import cn.heap.forum.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> selectAll(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public boolean register(RegisterRequest registerRequest) {
        // 1. 参数验证
        if (registerRequest == null || 
            registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty() ||
            registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty() ||
            registerRequest.getConfirmPassword() == null || registerRequest.getConfirmPassword().trim().isEmpty()) {
            return false;
        }
        
        // 2. 检查两次密码是否一致
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return false;
        }
        
        // 3. 检查用户名是否已存在
        if (isUsernameExists(registerRequest.getUsername())) {
            return false;
        }
        
        // 4. 创建用户对象
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // 密码加密
        user.setRoleId(registerRequest.getRoleId() != null ? registerRequest.getRoleId() : 1); // 默认角色ID为1
        user.setRegisterTime(LocalDateTime.now());
        
        // 5. 保存到数据库
        try {
            return userMapper.insert(user) > 0;
        } catch (Exception e) {
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
}
