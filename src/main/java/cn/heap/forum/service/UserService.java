package cn.heap.forum.service;

import cn.heap.forum.pojo.User;

import java.util.List;

public interface UserService {
    List<User> selectAll(Integer id);
}
