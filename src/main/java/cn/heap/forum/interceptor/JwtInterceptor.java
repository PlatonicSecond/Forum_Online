package cn.heap.forum.interceptor;

import cn.heap.forum.pojo.User;
import cn.heap.forum.service.UserService;
import cn.heap.forum.util.JwtUtil;
import cn.heap.forum.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 跨域预检请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 获取请求路径
        String requestURI = request.getRequestURI();


        // 白名单路径，不需要验证token
        if (isWhiteList(requestURI)) {
            return true;
        }

        // 从请求头获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 移除 "Bearer " 前缀
        }

        // 验证token
        if (token == null || !jwtUtil.validateToken(token)) {
            System.out.println("🚫 JWT拦截器: 令牌验证失败 - " + requestURI );
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\",\"data\":null}");
            return false;
        }

        System.out.println("✅ JWT拦截器: 令牌验证成功 - " + requestURI);

        // 从token中获取用户信息
        Integer userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        Integer roleId = jwtUtil.getRoleIdFromToken(token);

        System.out.println("🔍 JWT解析结果: userId=" + userId + ", username=" + username + ", roleId=" + roleId);

        if (userId != null) {
            // 查询用户信息并设置到ThreadLocal
            List<User> users = userService.selectAll(userId);
            if (!users.isEmpty()) {
                UserContext.setCurrentUser(users.get(0));
                System.out.println("📝 ThreadLocal设置成功: " + users.get(0).getUsername());
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除ThreadLocal中的用户信息，防止内存泄漏
        System.out.println("🧹 清除ThreadLocal用户信息");
        UserContext.clear();
    }

    /**
     * 判断是否为白名单路径
     */
    private boolean isWhiteList(String requestURI) {
        // 不需要验证token的路径（精确匹配）
        String[] exactWhiteList = {
                "/user/login",
                "/user/register",
                "/",
                "/index.html",
                "/addpost.html",
                "/login.html",
                "/test-login.html",
                "/static/post.html",
                "/favicon.ico",
                "/error",
                "/update.html"
        };

        // 前缀匹配的白名单（静态资源等）
        String[] prefixWhiteList = {
                "/js/",
                "/css/",
                "/images/",
                "/v2/api-docs",
                "/swagger-ui.html",
                "/swagger-resources",
                "/webjars"
        };

        // 精确匹配检查
        for (String path : exactWhiteList) {
            if (requestURI.equals(path)) {
                System.out.println("⚪ 白名单路径（精确匹配），跳过JWT验证: " + requestURI);
                return true;
            }
        }

        // 前缀匹配检查
        for (String path : prefixWhiteList) {
            if (requestURI.startsWith(path)) {
                System.out.println("⚪ 白名单路径（前缀匹配），跳过JWT验证: " + requestURI);
                return true;
            }
        }

        System.out.println("🔍 需要JWT验证的路径: " + requestURI);
        return false;
    }
} 