package cn.heap.forum.service.impl;

import cn.heap.forum.mapper.UserMapper;
import cn.heap.forum.pojo.User;
import cn.heap.forum.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAll(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("userId", id);

        return userMapper.selectList(queryWrapper);
    }

}
