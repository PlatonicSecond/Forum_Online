-- 创建数据库
CREATE DATABASE IF NOT EXISTS forum DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE forum;

-- 创建角色表
CREATE TABLE role (
                      role_id INT PRIMARY KEY AUTO_INCREMENT,
                      role_name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
                      description VARCHAR(200) COMMENT '角色描述'
);

-- 创建用户表
CREATE TABLE user (
                      user_id INT PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                      password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
                      avatar_path VARCHAR(255) DEFAULT NULL COMMENT '用户头像文件路径',
                      role_id INT NOT NULL COMMENT '用户角色（关联Role）',
                      register_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                      FOREIGN KEY (role_id) REFERENCES role(role_id)
);

-- 创建版块表
CREATE TABLE plate (
                       plate_id INT PRIMARY KEY AUTO_INCREMENT,
                       plate_name VARCHAR(100) NOT NULL UNIQUE COMMENT '版块名称',
                       description VARCHAR(500) COMMENT '版块描述'
);

-- 创建帖子表
CREATE TABLE post (
                      post_id INT PRIMARY KEY AUTO_INCREMENT,
                      title VARCHAR(200) COMMENT '帖子标题',
                      content TEXT COMMENT '帖子内容',
                      author_id INT NOT NULL COMMENT '发帖用户ID',
                      plate_id INT NOT NULL COMMENT '所属版块ID',
                      create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                      view_count INT DEFAULT 0 COMMENT '浏览次数',
                      img_path varchar(50) DEFAULT NULL COMMENT '图片路径',
                      FOREIGN KEY (author_id) REFERENCES user(user_id),
                      FOREIGN KEY (plate_id) REFERENCES plate(plate_id)
);

-- 创建评论表
CREATE TABLE comment (
                         comment_id INT PRIMARY KEY AUTO_INCREMENT,
                         content TEXT NOT NULL COMMENT '评论内容',
                         post_id INT NOT NULL COMMENT '所属帖子ID',
                         author_id INT NOT NULL COMMENT '评论用户ID',
                         user_id INT NOT NULL COMMENT '评论回复用户ID',
                         create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         FOREIGN KEY (post_id) REFERENCES post(post_id),
                         FOREIGN KEY (user_id) REFERENCES user(user_id),
                         FOREIGN KEY (author_id) REFERENCES user(user_id)
);

-- 插入默认角色数据
INSERT INTO role (role_name, description) VALUES
('普通用户', '普通论坛用户，可以发帖、评论'),
('管理员', '系统管理员，拥有最高权限');

-- 插入默认版块数据
INSERT INTO plate (plate_name, description) VALUES
('推荐', '只有500+浏览量的帖子才会在这里展示'),
('日常', '在这里分享你的日常生活趣事吧');

INSERT INTO forum.`user` (username,password,avatar_path,role_id,register_time) VALUES
('a','123','cd',1,'2025-07-09 10:31:57'),
('b','123','cd',1,'2025-07-09 10:31:57');