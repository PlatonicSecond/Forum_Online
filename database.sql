-- 创建数据库
CREATE DATABASE IF NOT EXISTS forum DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE forum;

-- 创建角色表
CREATE TABLE Role (
    roleId INT PRIMARY KEY AUTO_INCREMENT,
    roleName VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    description VARCHAR(200) COMMENT '角色描述',
    createTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 创建用户表
CREATE TABLE User (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    avatarPath VARCHAR(255) DEFAULT NULL COMMENT '用户头像文件路径',
    roleId INT NOT NULL COMMENT '用户角色（关联Role）',
    registerTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    FOREIGN KEY (roleId) REFERENCES Role(roleId)
);

-- 创建版块表
CREATE TABLE Plate (
    plateId INT PRIMARY KEY AUTO_INCREMENT,
    plateName VARCHAR(100) NOT NULL UNIQUE COMMENT '版块名称',
    description VARCHAR(500) COMMENT '版块描述',
    createTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 创建帖子表
CREATE TABLE Post (
    postId INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '帖子标题',
    content TEXT NOT NULL COMMENT '帖子内容',
    userId INT NOT NULL COMMENT '发帖用户ID',
    plateId INT NOT NULL COMMENT '所属版块ID',
    createTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    viewCount INT DEFAULT 0 COMMENT '浏览次数',
    FOREIGN KEY (userId) REFERENCES User(userId),
    FOREIGN KEY (plateId) REFERENCES Plate(plateId)
);

-- 创建评论表
CREATE TABLE Comment (
    commentId INT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL COMMENT '评论内容',
    postId INT NOT NULL COMMENT '所属帖子ID',
    userId INT NOT NULL COMMENT '评论用户ID',
    parentCommentId INT DEFAULT NULL COMMENT '父评论ID（用于回复）',
    createTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (postId) REFERENCES Post(postId),
    FOREIGN KEY (userId) REFERENCES User(userId),
    FOREIGN KEY (parentCommentId) REFERENCES Comment(commentId)
);

-- 插入默认角色数据
INSERT INTO Role (roleName, description) VALUES 
('普通用户', '普通论坛用户，可以发帖、评论'),
('版主', '版块管理员，可以管理版块内容'),
('管理员', '系统管理员，拥有最高权限');

-- 插入默认版块数据
INSERT INTO Plate (plateName, description) VALUES 
('技术讨论', '技术相关话题讨论'),
('生活分享', '生活经验和心得分享'),
('新手报到', '新用户介绍和问题咨询'),
('公告通知', '系统公告和重要通知');

-- 创建索引以提高查询性能
CREATE INDEX idx_user_username ON User(username);
CREATE INDEX idx_post_user ON Post(userId);
CREATE INDEX idx_post_plate ON Post(plateId);
CREATE INDEX idx_comment_post ON Comment(postId);
CREATE INDEX idx_comment_user ON Comment(userId); 