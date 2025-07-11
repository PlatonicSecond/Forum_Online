package cn.heap.forum.controller;

import cn.heap.forum.pojo.LoginRequest;
import cn.heap.forum.pojo.LoginResponse;
import cn.heap.forum.pojo.RegisterRequest;
import cn.heap.forum.pojo.UpdateUserRequest;
import cn.heap.forum.pojo.User;
import cn.heap.forum.service.UserService;
import cn.heap.forum.util.JwtUtil;
import cn.heap.forum.util.ServerResult;
import cn.heap.forum.util.UserContext;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@Api(tags = "user-controller")
public class UserController {

    @Value("${file.upload-dir}")
    private String path;

//    private String path = "src//main//resources//static//images//png_for_potrait//";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @ApiOperation(value = "用户登录", notes = "用户身份验证登录")
    @PostMapping("/user/login") // 登录接口,身份判断
    public ServerResult<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        try {
            Path Path = Paths.get(path);
            System.out.println("Path: " + Path);

            try {
                if (!Files.exists(Path)) {
                    System.out.println("路径：" + Path + " 不存在！");
                    Files.createDirectories(Path); // 自动创建所有不存在的父目录
                    System.out.println("路径：" + Path + "创建成功！");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


            System.out.println("=== 开始处理登录请求 ===");

            // 1. 参数基本验证
            if (loginRequest == null) {
                System.out.println("❌ 登录请求为空");
                return ServerResult.error(400, "登录信息不能为空");
            }

            System.out.println("📝 登录请求信息:");
            System.out.println("  - 用户名: " + loginRequest.getUsername());
            System.out.println("  - 密码: " + loginRequest.getPassword());

            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                System.out.println("❌ 用户名为空");
                return ServerResult.error(400, "用户名不能为空");
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                System.out.println("❌ 密码为空");
                return ServerResult.error(400, "密码不能为空");
            }

            System.out.println("✅ 参数验证通过");

            // 2. 执行登录验证
            System.out.println("🔍 开始验证用户身份...");
            User user = userService.login(loginRequest);
            if (user == null) {
                System.out.println("❌ 登录验证失败: 用户名或密码错误");
                return ServerResult.error(401, "用户名或密码错误");
            }

            System.out.println("✅ 登录验证成功!");
            System.out.println("📋 用户信息:");
            System.out.println("  - 用户ID: " + user.getUserId());
            System.out.println("  - 用户名: " + user.getUsername());
            System.out.println("  - 角色ID: " + user.getRoleId());

            // 3. 生成JWT令牌
            System.out.println("🔐 生成JWT令牌...");
            String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRoleId());
            System.out.println("✅ JWT令牌生成成功: " + token.substring(0, 20) + "...");

            // 4. 构造响应数据
            String avatarPath = user.getAvatarPath();

            System.out.println("avatarPath: " + avatarPath);
            try{
                if (avatarPath == null) {
                    avatarPath = path + "/1.png";
                    System.out.println("avatarPath: " + avatarPath);
                    File file = new File(avatarPath);
                    if (!file.exists()) {
                        System.out.println("file not exists: " + file.getAbsolutePath());
                    } else {
                        System.out.println("file exists: " + file.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


            String roleName = user.getRoleId() == 1 ? "管理员" : "普通用户";
            LoginResponse loginResponse = new LoginResponse(token, user.getUsername(), user.getRoleId(), roleName, avatarPath);

            System.out.println("🎉 登录成功! 返回响应数据");
            return ServerResult.success(loginResponse);

        } catch (Exception e) {
            System.out.println("❌ 登录过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            return ServerResult.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    @ApiOperation(value = "用户注册", notes = "新用户注册")
    @PostMapping("/user/register") // 注册接口
    public ServerResult<String> register(@RequestBody RegisterRequest registerRequest){
        try {
            // 1. 参数基本验证
            if (registerRequest == null) {
                return ServerResult.error(400, "注册信息不能为空");
            }

            if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
                return ServerResult.error(400, "用户名不能为空");
            }

            if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
                return ServerResult.error(400, "密码不能为空");
            }

            if (registerRequest.getConfirmPassword() == null || registerRequest.getConfirmPassword().trim().isEmpty()) {
                return ServerResult.error(400, "确认密码不能为空");
            }

            // 2. 用户名长度验证
            if (registerRequest.getUsername().length() < 3 || registerRequest.getUsername().length() > 50) {
                return ServerResult.error(400, "用户名长度必须在3-50个字符之间");
            }

            // 3. 密码强度验证
            if (registerRequest.getPassword().length() < 6) {
                return ServerResult.error(400, "密码长度至少6个字符");
            }

            // 4. 两次密码一致性验证
            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                return ServerResult.error(400, "两次输入的密码不一致");
            }

            // 5. 检查用户名是否已存在
            if (userService.isUsernameExists(registerRequest.getUsername())) {
                return ServerResult.error(409, "用户名已存在，请选择其他用户名");
            }

            // 6. 执行注册
            boolean success = userService.register(registerRequest);
            if (success) {
                return ServerResult.success("注册成功");
            } else {
                return ServerResult.error(500, "注册失败，请稍后重试");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResult.error(500, "服务器内部错误");
        }
    }

    @ApiOperation(value = "更新用户信息", notes = "更新用户个人信息")
    @PutMapping("/user/update")
    public ServerResult<String> updateUserInfo(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {
        try {
            System.out.println("=== 开始处理更新用户信息请求 ===");

            // 1. 从 ThreadLocal 获取当前用户 ID
            Integer currentUserId = UserContext.getCurrentUserId();
            if (currentUserId == null) {
                System.out.println("❌ 用户未登录");
                return ServerResult.error(401, "用户未登录");
            }

            System.out.println("📝 更新请求信息:");
            System.out.println("  - 当前用户ID: " + currentUserId);
            System.out.println("  - 新用户名: " + username);
            System.out.println("  - 是否修改密码: " + (newPassword != null && !newPassword.trim().isEmpty()));
            System.out.println("  - 头像文件: " + (avatar != null ? avatar.getOriginalFilename() : "无"));

            // 2. 处理头像文件
            String fileName = UserContext.getCurrentUsername() + ".png";
            if (avatar != null && !avatar.isEmpty()) {
                String uploadDir = "D:/load/images/png_for_potrait";

                // 调用工具方法保存文件
                String avatarPath = saveAvatarWithTherapyName(avatar, uploadDir, fileName);
            }

            // 3. 构造 UpdateUserRequest 对象
            UpdateUserRequest updateRequest = new UpdateUserRequest();
            updateRequest.setUsername(username);
            updateRequest.setPassword(password);
            updateRequest.setNewPassword(newPassword);
            updateRequest.setAvatarPath(fileName);


            // 4. 执行更新
            boolean success = userService.updateUserInfo(currentUserId, updateRequest);
            if (success) {
                System.out.println("🎉 用户信息更新成功!");
                return ServerResult.success("用户信息更新成功");
            } else {
                System.out.println("❌ 用户信息更新失败");
                return ServerResult.error(500, "更新失败，请检查输入信息");
            }

        } catch (Exception e) {
            System.out.println("❌ 更新用户信息异常: " + e.getMessage());
            e.printStackTrace();
            return ServerResult.error(500, "服务器内部错误: " + e.getMessage());
        }
    }
//    @PutMapping("/user/update")
//    public ServerResult<String> updateUserInfo(@RequestBody UpdateUserRequest updateRequest){
//        try {
//            System.out.println("=== 开始处理更新用户信息请求 ===");
//
//            // 1. 从ThreadLocal获取当前用户ID
//            Integer currentUserId = UserContext.getCurrentUserId();
//            if (currentUserId == null) {
//                System.out.println("❌ 用户未登录");
//                return ServerResult.error(401, "用户未登录");
//            }
//
//            System.out.println("📝 更新请求信息:");
//            System.out.println("  - 当前用户ID: " + currentUserId);
//            System.out.println("  - 新用户名: " + updateRequest.getUsername());
//            System.out.println("  - 是否修改密码: " + (updateRequest.getNewPassword() != null && !updateRequest.getNewPassword().trim().isEmpty()));
//            System.out.println("  - 头像路径: " + updateRequest.getAvatarPath());
//
//            // 2. 参数基本验证
//            if (updateRequest == null) {
//                System.out.println("❌ 更新请求为空");
//                return ServerResult.error(400, "更新信息不能为空");
//            }
//
//
//
//            // 3. 执行更新
//            boolean success = userService.updateUserInfo(currentUserId, updateRequest);
//            if (success) {
//                System.out.println("🎉 用户信息更新成功!");
//                return ServerResult.success("用户信息更新成功");
//            } else {
//                System.out.println("❌ 用户信息更新失败");
//                return ServerResult.error(500, "更新失败，请检查输入信息");
//            }
//
//        } catch (Exception e) {
//            System.out.println("❌ 更新用户信息异常: " + e.getMessage());
//            e.printStackTrace();
//            return ServerResult.error(500, "服务器内部错误: " + e.getMessage());
//        }
//    }

    @ApiOperation(value = "获取用户信息", notes = "获取当前登录用户的详细信息")
    @GetMapping("/user/info")
    public ServerResult<User> getUserInfo(){
        try {
            System.out.println("=== 获取用户信息 ===");

            // 从ThreadLocal获取当前用户ID
            Integer currentUserId = UserContext.getCurrentUserId();
            if (currentUserId == null) {
                System.out.println("❌ 用户未登录");
                return ServerResult.error(401, "用户未登录");
            }

            User user = userService.getUserById(currentUserId);
            if (user != null) {
                // 不返回密码信息
                user.setPassword(null);

                // 设置默认头像
                if (user.getAvatarPath() == null) {
                    user.setAvatarPath(path + "/1.png");
                } else {
                    user.setAvatarPath("images/png_for_potrait/" + user.getAvatarPath());
                }

                System.out.println("后端用户头像路径：" + user.getAvatarPath());
                System.out.println("✅ 成功获取用户信息: " + user.getUsername());
                System.out.println("111:" + user);
                return ServerResult.success(user);
            } else {
                System.out.println("❌ 用户不存在");
                return ServerResult.error(404, "用户不存在");
            }
        } catch (Exception e) {
            System.out.println("❌ 获取用户信息异常: " + e.getMessage());
            e.printStackTrace();
            return ServerResult.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    @GetMapping("user/info2")
    public ServerResult<User> getUserInfo2(){
        return ServerResult.success(userService.getUserById(7));
    }

    @ApiOperation(value = "验证JWT和ThreadLocal", notes = "检查JWT令牌和ThreadLocal用户上下文是否正常工作")
    @GetMapping("/user/verify")
    public ServerResult<String> verifyJwtAndThreadLocal(){
        try {
            System.out.println("=== JWT和ThreadLocal验证API ===");

            // 从ThreadLocal获取用户信息
            Integer currentUserId = UserContext.getCurrentUserId();
            String currentUsername = UserContext.getCurrentUsername();
            Integer currentRoleId = UserContext.getCurrentUserRoleId();
            boolean isLoggedIn = UserContext.isLoggedIn();

            System.out.println("🔍 ThreadLocal状态检查:");
            System.out.println("  - 用户ID: " + currentUserId);
            System.out.println("  - 用户名: " + currentUsername);
            System.out.println("  - 角色ID: " + currentRoleId);
            System.out.println("  - 登录状态: " + isLoggedIn);

            if (!isLoggedIn) {
                System.out.println("❌ ThreadLocal中没有用户信息");
                return ServerResult.error(401, "ThreadLocal中没有用户信息，JWT拦截器可能未正常工作");
            }

            String message = String.format(
                    "✅ JWT和ThreadLocal验证成功！\n" +
                            "用户ID: %d\n" +
                            "用户名: %s\n" +
                            "角色ID: %d\n" +
                            "这证明:\n" +
                            "1. JWT令牌验证正常\n" +
                            "2. JWT拦截器正常工作\n" +
                            "3. ThreadLocal用户上下文正常设置",
                    currentUserId, currentUsername, currentRoleId
            );

            System.out.println("✅ 验证成功！");
            return ServerResult.success(message);

        } catch (Exception e) {
            System.out.println("❌ 验证异常: " + e.getMessage());
            e.printStackTrace();
            return ServerResult.error(500, "验证失败: " + e.getMessage());
        }
    }

    private String saveAvatarWithTherapyName(MultipartFile avatar, String uploadDir, String therapyName) throws IOException, IOException {
        // 创建目录
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

//        // 原始文件名
//        String originalFilename = avatar.getOriginalFilename();
//        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//
//        // 新文件名：用户ID_治病名称_UUID.扩展名
//        String newFilename = therapyName + "_" + UUID.randomUUID().toString() + fileExtension;

        // 保存文件
        Path targetPath = uploadPath.resolve(therapyName);
        Files.copy(avatar.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // 返回完整路径（或相对路径，根据你的业务需要）
        return targetPath.toString();
    }
}
