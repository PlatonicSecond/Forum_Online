package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    private String username;
    private String password;
    @TableField("avatar_path")  // 指定数据库字段名
    private String avatarPath;
    @TableField("role_id")
    private Integer roleId;
    @TableField("register_time")
    private LocalDateTime registerTime;
}

