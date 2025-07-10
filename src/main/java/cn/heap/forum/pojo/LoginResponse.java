package cn.heap.forum.pojo;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String username;
    private Integer roleId;
    private String roleName;
    
    public LoginResponse(String token, String username, Integer roleId, String roleName) {
        this.token = token;
        this.username = username;
        this.roleId = roleId;
        this.roleName = roleName;
    }
} 