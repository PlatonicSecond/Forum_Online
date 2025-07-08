package cn.heap.forum.util;

import cn.heap.forum.pojo.User;

/**
 * 用户上下文工具类
 * 使用ThreadLocal存储当前请求的用户信息
 */
public class UserContext {
    
    private static final ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<>();
    
    /**
     * 设置当前用户
     */
    public static void setCurrentUser(User user) {
        USER_THREAD_LOCAL.set(user);
    }
    
    /**
     * 获取当前用户
     */
    public static User getCurrentUser() {
        return USER_THREAD_LOCAL.get();
    }
    
    /**
     * 获取当前用户ID
     */
    public static Integer getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }
    
    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        User user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }
    
    /**
     * 获取当前用户角色ID
     */
    public static Integer getCurrentUserRoleId() {
        User user = getCurrentUser();
        return user != null ? user.getRoleId() : null;
    }
    
    /**
     * 清除当前用户信息
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
    
    /**
     * 检查是否已登录
     */
    public static boolean isLoggedIn() {
        return getCurrentUser() != null;
    }
} 