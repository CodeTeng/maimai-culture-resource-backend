/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : culture-resource

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 31/03/2024 22:47:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`
(
    `id`               bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`          bigint(0)                                                     NOT NULL COMMENT '作者id',
    `article_content`  longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL COMMENT '内容',
    `article_cover`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '封面图',
    `article_title`    varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
    `article_abstract` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '文章摘要，如果该字段为空，默认取文章的前500个字符作为摘要',
    `latest_answer_id` bigint(0)                                                     NULL     DEFAULT NULL COMMENT '最新的回复id',
    `reply_times`      int(0) UNSIGNED                                               NOT NULL DEFAULT 0 COMMENT '评论数量',
    `fav_times`        int(0)                                                        NOT NULL DEFAULT 0 COMMENT '收藏数量',
    `type`             tinyint(0)                                                    NULL     DEFAULT NULL COMMENT '类型 1-文章 2-视频',
    `url`              varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '视频地址',
    `create_time`      datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '发表时间',
    `update_time`      datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted`          tinyint(0)                                                    NOT NULL DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '文化资源表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article`
VALUES (1, 1, '234', 'https://muziteng-1310538376.cos.ap-beijing.myqcloud.com/article_cover/1/p87KLCDX-bg2.jpg', '234',
        NULL, 38, 6, 1, 1, '', '2024-03-22 23:49:19', '2024-03-31 15:58:05', 0);
INSERT INTO `article`
VALUES (2, 1, '234', 'https://muziteng-1310538376.cos.ap-beijing.myqcloud.com/article_cover/1/p87KLCDX-bg2.jpg', '23',
        NULL, 26, 2, 1, 1, '', '2024-03-22 23:49:32', '2024-03-30 18:27:11', 0);
INSERT INTO `article`
VALUES (3, 1, '2323', 'https://muziteng-1310538376.cos.ap-beijing.myqcloud.com/article_cover/1/p87KLCDX-bg2.jpg', '23',
        NULL, 27, 2, 1, 1, '', '2024-03-22 23:49:32', '2024-03-30 18:27:16', 0);
INSERT INTO `article`
VALUES (4, 1, '23', 'https://muziteng-1310538376.cos.ap-beijing.myqcloud.com/article_cover/1/p87KLCDX-bg2.jpg', '2',
        NULL, 19, 1, 1, 1, '', '2024-03-22 23:49:48', '2024-03-30 23:03:47', 0);
INSERT INTO `article`
VALUES (5, 1, '23', 'https://muziteng-1310538376.cos.ap-beijing.myqcloud.com/article_cover/1/p87KLCDX-bg2.jpg', '2',
        NULL, 20, 1, 1, 1, '', '2024-03-22 23:49:48', '2024-03-30 18:25:35', 0);
INSERT INTO `article`
VALUES (6, 1, '23', 'https://muziteng-1310538376.cos.ap-beijing.myqcloud.com/article_cover/1/p87KLCDX-bg2.jpg', '34',
        NULL, 21, 1, 0, 1, '', '2024-03-22 23:49:48', '2024-03-30 18:25:37', 0);
INSERT INTO `article`
VALUES (7, 18, '23', 'https://muziteng-1310538376.cos.ap-beijing.myqcloud.com/article_cover/1/p87KLCDX-bg2.jpg', '5',
        NULL, 22, 1, 0, 1, '', '2024-03-22 23:49:48', '2024-03-30 18:25:40', 0);
INSERT INTO `article`
VALUES (8, 1, '23', 'https://muziteng-1310538376.cos.ap-beijing.myqcloud.com/article_cover/1/p87KLCDX-bg2.jpg', '6',
        NULL, 23, 1, 1, 1, '', '2024-03-22 23:49:48', '2024-03-31 00:01:45', 0);
INSERT INTO `article`
VALUES (9, 1, 'video', 'https://muziteng-1310538376.cos.ap-beijing.myqcloud.com/article_cover/1/p87KLCDX-bg2.jpg',
        'test_video', NULL, 24, 1, 0, 2,
        'http://1310538376.vod2.myqcloud.com/7bc4e907vodcq1310538376/e68e92be1397757887804920785/sKJZcaQKlAgA.mp4',
        '2024-03-28 22:54:41', '2024-03-31 00:18:02', 0);

-- ----------------------------
-- Table structure for article_fav
-- ----------------------------
DROP TABLE IF EXISTS `article_fav`;
CREATE TABLE `article_fav`
(
    `id`          bigint(0)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(0)   NOT NULL COMMENT '用户Id',
    `article_id`  bigint(0)   NOT NULL COMMENT '文章id',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '收藏时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 18
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '文章收藏表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_fav
-- ----------------------------
INSERT INTO `article_fav`
VALUES (4, 1, 1, '2024-03-28 21:08:32');
INSERT INTO `article_fav`
VALUES (5, 1, 2, '2024-03-28 21:08:44');
INSERT INTO `article_fav`
VALUES (6, 1, 3, '2024-03-28 21:08:46');
INSERT INTO `article_fav`
VALUES (9, 1, 5, '2024-03-28 21:09:07');
INSERT INTO `article_fav`
VALUES (10, 1, 4, '2024-03-30 23:03:47');
INSERT INTO `article_fav`
VALUES (17, 1, 8, '2024-03-31 00:01:45');

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`
(
    `id`         bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `article_id` bigint(0) NOT NULL COMMENT '文章id',
    `tag_id`     bigint(0) NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 35
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '文章标签中间表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_tag
-- ----------------------------
INSERT INTO `article_tag`
VALUES (1, 1, 1);
INSERT INTO `article_tag`
VALUES (2, 1, 2);
INSERT INTO `article_tag`
VALUES (3, 1, 3);
INSERT INTO `article_tag`
VALUES (4, 1, 4);
INSERT INTO `article_tag`
VALUES (5, 1, 5);
INSERT INTO `article_tag`
VALUES (6, 2, 2);
INSERT INTO `article_tag`
VALUES (7, 2, 3);
INSERT INTO `article_tag`
VALUES (8, 2, 4);
INSERT INTO `article_tag`
VALUES (9, 2, 5);
INSERT INTO `article_tag`
VALUES (10, 3, 1);
INSERT INTO `article_tag`
VALUES (11, 3, 2);
INSERT INTO `article_tag`
VALUES (12, 3, 3);
INSERT INTO `article_tag`
VALUES (13, 3, 4);
INSERT INTO `article_tag`
VALUES (14, 3, 5);
INSERT INTO `article_tag`
VALUES (15, 4, 1);
INSERT INTO `article_tag`
VALUES (16, 4, 2);
INSERT INTO `article_tag`
VALUES (17, 4, 3);
INSERT INTO `article_tag`
VALUES (18, 4, 4);
INSERT INTO `article_tag`
VALUES (19, 4, 5);
INSERT INTO `article_tag`
VALUES (20, 4, 6);
INSERT INTO `article_tag`
VALUES (21, 5, 1);
INSERT INTO `article_tag`
VALUES (22, 6, 1);
INSERT INTO `article_tag`
VALUES (23, 7, 1);
INSERT INTO `article_tag`
VALUES (24, 8, 1);
INSERT INTO `article_tag`
VALUES (25, 1, 7);
INSERT INTO `article_tag`
VALUES (26, 1, 8);
INSERT INTO `article_tag`
VALUES (27, 1, 9);
INSERT INTO `article_tag`
VALUES (28, 2, 9);
INSERT INTO `article_tag`
VALUES (29, 9, 1);
INSERT INTO `article_tag`
VALUES (30, 9, 2);
INSERT INTO `article_tag`
VALUES (31, 9, 3);
INSERT INTO `article_tag`
VALUES (32, 9, 4);
INSERT INTO `article_tag`
VALUES (33, 9, 5);
INSERT INTO `article_tag`
VALUES (34, 9, 6);

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`
(
    `id`          bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `from_id`     bigint(0)                                                      NOT NULL COMMENT '发送消息人',
    `to_id`       bigint(0)                                                      NOT NULL COMMENT '接收消息人',
    `text`        varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '聊天记录',
    `chat_type`   tinyint(0)                                                     NOT NULL COMMENT '聊天类型 1-私聊 2-群聊',
    `create_time` datetime(0)                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '聊天消息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat
-- ----------------------------

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `from_id`     bigint(0)                                                     NOT NULL COMMENT '发送申请的用户id',
    `receive_id`  bigint(0)                                                     NOT NULL COMMENT '接收申请的用户id ',
    `is_read`     tinyint(0)                                                    NOT NULL DEFAULT 0 COMMENT '是否已读(0-未读 1-已读)',
    `status`      tinyint(0)                                                    NOT NULL DEFAULT 0 COMMENT '申请状态 默认0 （0-未通过 1-已同意 2-已过期 3-已撤销）',
    `remark`      varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '好友申请备注信息',
    `create_time` datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted`     tinyint(0)                                                    NOT NULL DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '好友申请管理表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of friends
-- ----------------------------
INSERT INTO `friends`
VALUES (2, 1, 2, 1, 1, '你好，我是test123122222222222222222222222', '2024-03-23 22:14:50', '2024-03-25 18:49:48', 0);
INSERT INTO `friends`
VALUES (5, 4, 1, 1, 1, '你好，我是test', '2024-03-25 15:37:21', '2024-03-25 18:48:58', 0);
INSERT INTO `friends`
VALUES (6, 1, 3, 1, 1, '你好，我是test', '2024-03-25 16:33:33', '2024-03-25 18:48:58', 0);
INSERT INTO `friends`
VALUES (7, 7, 1, 0, 1, '你好啊', '2024-03-25 19:00:33', '2024-03-25 20:26:27', 1);
INSERT INTO `friends`
VALUES (8, 1, 6, 0, 3, '你好啊，帅哥', '2024-03-25 19:01:02', '2024-03-25 19:11:45', 0);
INSERT INTO `friends`
VALUES (9, 1, 5, 0, 3, '你好啊，帅哥', '2024-03-25 19:01:33', '2024-03-25 19:01:33', 0);
INSERT INTO `friends`
VALUES (10, 1, 5, 0, 1, '我是用户teng', '2024-03-25 20:53:44', '2024-03-25 20:53:44', 0);

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`
(
    `id`              bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `article_id`      bigint(0)                                                     NOT NULL COMMENT '文章id',
    `answer_id`       bigint(0)                                                     NOT NULL DEFAULT 0 COMMENT '回复的上级评论id',
    `user_id`         bigint(0)                                                     NOT NULL COMMENT '回答者id',
    `content`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回答内容',
    `target_user_id`  bigint(0)                                                     NOT NULL DEFAULT 0 COMMENT '回复的目标用户id',
    `target_reply_id` bigint(0)                                                     NOT NULL DEFAULT 0 COMMENT '回复的目标回复id',
    `reply_times`     int(0)                                                        NOT NULL DEFAULT 0 COMMENT '评论数量',
    `hidden`          bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否被隐藏，默认false',
    `create_time`     datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`     datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted`         tinyint(0)                                                    NOT NULL DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_article_id` (`article_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 41
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '回复评论表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reply
-- ----------------------------
INSERT INTO `reply`
VALUES (16, 1, 0, 2, 'teng2在文章1下面评论了', 0, 0, 2, b'0', '2024-03-30 18:25:16', '2024-03-30 18:37:35', 0);
INSERT INTO `reply`
VALUES (17, 2, 0, 2, 'teng2在文章2下面评论了', 0, 0, 0, b'0', '2024-03-30 18:25:25', '2024-03-30 18:26:20', 0);
INSERT INTO `reply`
VALUES (18, 3, 0, 2, 'teng2在文章3下面评论了', 0, 0, 0, b'0', '2024-03-30 18:25:28', '2024-03-30 18:26:20', 0);
INSERT INTO `reply`
VALUES (19, 4, 0, 2, 'teng2在文章4下面评论了', 0, 0, 0, b'0', '2024-03-30 18:25:32', '2024-03-30 18:26:20', 0);
INSERT INTO `reply`
VALUES (20, 5, 0, 2, 'teng2在文章5下面评论了', 0, 0, 0, b'0', '2024-03-30 18:25:35', '2024-03-30 18:26:20', 0);
INSERT INTO `reply`
VALUES (21, 6, 0, 2, 'teng2在文章6下面评论了', 0, 0, 0, b'0', '2024-03-30 18:25:37', '2024-03-30 18:26:20', 0);
INSERT INTO `reply`
VALUES (22, 7, 0, 2, 'teng2在文章7下面评论了', 0, 0, 0, b'0', '2024-03-30 18:25:40', '2024-03-30 18:26:20', 0);
INSERT INTO `reply`
VALUES (23, 8, 0, 2, 'teng2在文章8下面评论了', 0, 0, 0, b'0', '2024-03-30 18:25:53', '2024-03-30 18:25:53', 0);
INSERT INTO `reply`
VALUES (24, 9, 0, 2, 'teng2在文章9下面评论了', 0, 0, 0, b'0', '2024-03-30 18:25:57', '2024-03-30 18:25:57', 0);
INSERT INTO `reply`
VALUES (25, 1, 0, 1, 'teng在文章1下面评论了', 0, 0, 0, b'0', '2024-03-30 18:26:52', '2024-03-30 18:26:52', 0);
INSERT INTO `reply`
VALUES (26, 2, 0, 1, 'teng在文章2下面评论了', 0, 0, 1, b'0', '2024-03-30 18:27:11', '2024-03-30 18:38:52', 0);
INSERT INTO `reply`
VALUES (27, 3, 0, 1, 'teng在文章3下面评论了', 0, 0, 0, b'0', '2024-03-30 18:27:16', '2024-03-30 18:27:16', 0);
INSERT INTO `reply`
VALUES (28, 3, 18, 1, 'teng回复了teng2在文章3下面的评论', 2, 18, 0, b'0', '2024-03-30 18:30:06', '2024-03-30 18:30:58',
        0);
INSERT INTO `reply`
VALUES (29, 1, 16, 1, 'teng回复了teng2在文章1下面的评论', 2, 16, 0, b'0', '2024-03-30 18:31:07', '2024-03-30 18:31:07',
        0);
INSERT INTO `reply`
VALUES (30, 1, 16, 2, 'teng2回复了teng在文章1下面对我的评论', 1, 29, 0, b'0', '2024-03-30 18:32:45',
        '2024-03-30 18:32:45', 0);
INSERT INTO `reply`
VALUES (31, 2, 26, 2, 'teng2回复了teng在文章2下面的评论', 1, 26, 0, b'0', '2024-03-30 18:38:52', '2024-03-30 18:38:52',
        0);
INSERT INTO `reply`
VALUES (32, 1, 0, 1, '输入评论', 0, 0, 0, b'0', '2024-03-31 12:05:47', '2024-03-31 12:05:47', 0);
INSERT INTO `reply`
VALUES (33, 1, 0, 1, '输入test评论', 0, 0, 0, b'0', '2024-03-31 12:06:24', '2024-03-31 12:06:24', 0);
INSERT INTO `reply`
VALUES (34, 1, 0, 1, '评论', 0, 0, 0, b'0', '2024-03-31 12:07:41', '2024-03-31 12:07:41', 0);
INSERT INTO `reply`
VALUES (35, 1, 0, 1, '111', 0, 0, 1, b'0', '2024-03-31 12:07:47', '2024-03-31 15:58:05', 1);
INSERT INTO `reply`
VALUES (36, 1, 35, 1, 'teng回复teng的111评论', 1, 35, 0, b'0', '2024-03-31 12:16:04', '2024-03-31 15:58:05', 1);
INSERT INTO `reply`
VALUES (38, 1, 0, 1, '123123', 0, 0, 2, b'0', '2024-03-31 15:28:12', '2024-03-31 15:59:04', 0);
INSERT INTO `reply`
VALUES (39, 1, 38, 1, '123', 1, 38, 0, b'0', '2024-03-31 15:58:39', '2024-03-31 15:58:39', 0);
INSERT INTO `reply`
VALUES (40, 1, 38, 1, '123', 1, 38, 0, b'0', '2024-03-31 15:59:04', '2024-03-31 15:59:04', 0);

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `tag_name`    varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名',
    `create_time` datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted`     tinyint(0)                                                    NOT NULL DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '标签表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag`
VALUES (1, '红色文化', '2024-03-28 14:48:17', '2024-03-28 14:48:17', 0);
INSERT INTO `tag`
VALUES (2, '抗日精神', '2024-03-28 14:48:17', '2024-03-28 14:48:17', 0);
INSERT INTO `tag`
VALUES (3, '红色草原', '2024-03-28 14:48:17', '2024-03-28 14:48:17', 0);
INSERT INTO `tag`
VALUES (4, '长征精神', '2024-03-28 14:50:00', '2024-03-28 14:50:00', 0);
INSERT INTO `tag`
VALUES (5, '革命精神', '2024-03-28 14:50:00', '2024-03-28 14:50:00', 0);
INSERT INTO `tag`
VALUES (6, '乡村振兴', '2024-03-28 14:50:00', '2024-03-28 14:50:00', 0);
INSERT INTO `tag`
VALUES (7, '红色基因', '2024-03-28 14:50:00', '2024-03-28 14:50:00', 0);
INSERT INTO `tag`
VALUES (8, '民族团结', '2024-03-28 14:50:00', '2024-03-28 14:50:00', 0);
INSERT INTO `tag`
VALUES (9, '红色草原', '2024-03-28 14:50:22', '2024-03-28 14:50:22', 0);
INSERT INTO `tag`
VALUES (10, '草原文化', '2024-03-28 14:50:42', '2024-03-28 14:50:42', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`             bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `username`       varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '用户账号',
    `user_password`  varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '用户密码',
    `user_role`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT 'user' COMMENT '用户角色 user/admin/ban',
    `user_phone`     varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT NULL COMMENT '用户手机号',
    `user_real_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT NULL COMMENT '用户真实姓名',
    `user_gender`    tinyint(0)                                                     NOT NULL DEFAULT 0 COMMENT '性别：0-男性，1-女性',
    `user_age`       tinyint(0) UNSIGNED                                            NULL     DEFAULT NULL COMMENT '用户年龄',
    `user_email`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT NULL COMMENT '用户邮箱',
    `user_avatar`    varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '用户头像',
    `user_birthday`  date                                                           NULL     DEFAULT NULL COMMENT '用户生日',
    `user_profile`   varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '用户简介',
    `tags`           varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL COMMENT '用户选择的标签 json 列表',
    `user_status`    tinyint(0)                                                     NOT NULL DEFAULT 1 COMMENT '账号状态：0-禁用 1-正常',
    `create_time`    datetime(0)                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`    datetime(0)                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted`        tinyint(0)                                                     NOT NULL DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_pk` (`username`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (1, 'teng', '2003a5eab2b15a92088a06a85bbcfd49', 'user', '15290654216', '木子Teng', 0, 22,
        'teng2002823@outlook.com',
        'https://muziteng-1310538376.cos.ap-beijing.myqcloud.com/user_avatar/1/csu9KFk0-avatar_self.png', '2002-08-23',
        '我是大帅哥，欢迎光临', '[\"红色文化\",\"红色草原\",\"长征精神\",\"乡村振兴\"]', 1, '2024-03-23 00:35:46',
        '2024-03-31 22:43:35', 0);
INSERT INTO `user`
VALUES (2, 'teng2', '2003a5eab2b15a92088a06a85bbcfd49', 'user', NULL, NULL, 1, 21, NULL,
        'https://img.zcool.cn/community/01cfd95d145660a8012051cdb52093.png@2o.png', NULL, NULL, NULL, 1,
        '2024-03-23 17:19:47', '2024-03-25 16:13:51', 0);
INSERT INTO `user`
VALUES (3, 'teng3', '2003a5eab2b15a92088a06a85bbcfd49', 'user', '15290654216', 'test2', 1, 23, NULL,
        'https://img.zcool.cn/community/01cfd95d145660a8012051cdb52093.png@2o.png', NULL,
        '我也很帅11111111111111111111111111111111111111111111111',
        '[\"红色文化\",\"抗日精神\",\"红色草原\",\"长征精神\"]', 1, '2024-03-23 22:27:43', '2024-03-31 22:28:05', 0);
INSERT INTO `user`
VALUES (4, 'teng4', '2003a5eab2b15a92088a06a85bbcfd49', 'user', NULL, NULL, 0, NULL, NULL,
        'https://img.zcool.cn/community/01cfd95d145660a8012051cdb52093.png@2o.png', NULL, NULL, NULL, 1,
        '2024-03-23 22:27:47', '2024-03-23 22:27:47', 0);
INSERT INTO `user`
VALUES (5, 'teng5', '2003a5eab2b15a92088a06a85bbcfd49', 'user', NULL, NULL, 0, NULL, NULL,
        'https://img.zcool.cn/community/01cfd95d145660a8012051cdb52093.png@2o.png', NULL, NULL, NULL, 1,
        '2024-03-23 22:27:49', '2024-03-23 22:27:49', 0);
INSERT INTO `user`
VALUES (6, 'teng6', '2003a5eab2b15a92088a06a85bbcfd49', 'user', NULL, NULL, 0, NULL, NULL,
        'https://img.zcool.cn/community/01cfd95d145660a8012051cdb52093.png@2o.png', NULL, NULL, NULL, 1,
        '2024-03-23 22:27:52', '2024-03-23 22:27:52', 0);
INSERT INTO `user`
VALUES (7, 'teng7', '2003a5eab2b15a92088a06a85bbcfd49', 'user', NULL, NULL, 0, NULL, NULL,
        'https://img.zcool.cn/community/01cfd95d145660a8012051cdb52093.png@2o.png', NULL, NULL, NULL, 1,
        '2024-03-23 22:27:56', '2024-03-23 22:27:56', 0);
INSERT INTO `user`
VALUES (8, 'teng8', '2003a5eab2b15a92088a06a85bbcfd49', 'user', NULL, NULL, 0, NULL, NULL,
        'https://img.zcool.cn/community/01cfd95d145660a8012051cdb52093.png@2o.png', NULL, NULL, NULL, 1,
        '2024-03-23 22:27:58', '2024-03-23 22:27:58', 0);
INSERT INTO `user`
VALUES (9, 'teng9', '2003a5eab2b15a92088a06a85bbcfd49', 'user', NULL, NULL, 0, NULL, NULL,
        'https://img.zcool.cn/community/01cfd95d145660a8012051cdb52093.png@2o.png', NULL, NULL, NULL, 1,
        '2024-03-23 22:28:01', '2024-03-23 22:28:01', 0);
INSERT INTO `user`
VALUES (10, 'teng10', '2003a5eab2b15a92088a06a85bbcfd49', 'user', NULL, NULL, 0, NULL, NULL,
        'https://img.zcool.cn/community/01cfd95d145660a8012051cdb52093.png@2o.png', NULL, NULL, NULL, 1,
        '2024-03-23 22:28:04', '2024-03-23 22:28:04', 0);

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow`
(
    `id`             bigint(0)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`        bigint(0)   NOT NULL COMMENT '关注人Id',
    `user_follow_id` bigint(0)   NOT NULL COMMENT '被关注人id',
    `create_time`    datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '关注时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户关注表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_follow
-- ----------------------------
INSERT INTO `user_follow`
VALUES (1, 1, 2, '2024-03-23 19:26:44');
INSERT INTO `user_follow`
VALUES (3, 2, 1, '2024-03-23 22:14:50');
INSERT INTO `user_follow`
VALUES (5, 3, 1, '2024-03-23 23:25:59');
INSERT INTO `user_follow`
VALUES (11, 1, 3, '2024-03-25 15:30:06');
INSERT INTO `user_follow`
VALUES (12, 4, 1, '2024-03-25 15:33:40');
INSERT INTO `user_follow`
VALUES (13, 1, 4, '2024-03-25 15:37:21');
INSERT INTO `user_follow`
VALUES (17, 5, 1, '2024-03-25 21:02:43');
INSERT INTO `user_follow`
VALUES (18, 1, 5, '2024-03-25 21:02:43');

SET FOREIGN_KEY_CHECKS = 1;
