package cn.heap.forum.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    private Integer id;
    private String username;
    private String password;
    private String avatarPath;
    @TableField("roleId")
    private Integer roleId;
    private LocalDateTime registerTime;

}