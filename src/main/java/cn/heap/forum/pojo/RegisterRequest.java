package cn.heap.forum.pojo;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private Integer roleId;
} 