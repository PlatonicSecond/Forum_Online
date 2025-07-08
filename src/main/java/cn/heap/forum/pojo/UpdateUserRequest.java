package cn.heap.forum.pojo;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String password;
    private String newPassword;
    private String confirmNewPassword;
    private String avatarPath;
} 