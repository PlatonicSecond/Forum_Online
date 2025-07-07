package cn.heap.forum;

import cn.heap.forum.mapper.UserMapper;
import cn.heap.forum.pojo.User;
import cn.heap.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
public class DatabaseConnectionTest {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserService userService;
    
    /**
     * 测试数据库连接
     */
    @Test
    public void testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ 数据库连接成功!");
            System.out.println("数据库URL: " + connection.getMetaData().getURL());
            System.out.println("数据库用户: " + connection.getMetaData().getUserName());
            System.out.println("数据库驱动: " + connection.getMetaData().getDriverName());
        } catch (SQLException e) {
            System.err.println("❌ 数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试用户表查询
     */
    @Test
    public void testUserQuery() {
        try {
            List<User> users = userMapper.selectList(null);
            System.out.println("✅ 用户表查询成功!");
            System.out.println("用户数量: " + users.size());
            
            for (User user : users) {
                System.out.println("用户ID: " + user.getUserId() + 
                                   ", 用户名: " + user.getUsername() + 
                                   ", 角色ID: " + user.getRoleId() + 
                                   ", 注册时间: " + user.getRegisterTime());
            }
        } catch (Exception e) {
            System.err.println("❌ 用户表查询失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试用户名是否存在检查
     */
    @Test
    public void testUsernameCheck() {
        try {
            boolean exists = userService.isUsernameExists("admin");
            System.out.println("✅ 用户名检查功能正常!");
            System.out.println("用户名 'admin' 是否存在: " + exists);
        } catch (Exception e) {
            System.err.println("❌ 用户名检查失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 