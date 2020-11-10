/*
 Navicat Premium Data Transfer

 Source Server         : LocalMySQL
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : block

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 09/11/2020 18:02:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authority_menu
-- ----------------------------
DROP TABLE IF EXISTS `authority_menu`;
CREATE TABLE `authority_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求路径',
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父菜单id',
  `update_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `url_pre` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由(前端自己匹配用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authority_menu
-- ----------------------------
INSERT INTO `authority_menu` VALUES (1, '/security-manage/user-manage/findAll', '用户管理-查找全部用户', 0, NULL, NULL, NULL);
INSERT INTO `authority_menu` VALUES (2, '/security-manage/user-manage/findAll1', '用户管理-查找全部用户', 0, NULL, NULL, NULL);
INSERT INTO `authority_menu` VALUES (3, '/login', '登录', 0, NULL, NULL, NULL);
INSERT INTO `authority_menu` VALUES (4, '/public/hello', 'hello', 0, NULL, NULL, NULL);
INSERT INTO `authority_menu` VALUES (5, '/public/hello1', 'hello1', 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for authority_role
-- ----------------------------
DROP TABLE IF EXISTS `authority_role`;
CREATE TABLE `authority_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称(必须以ROLE_起始命名)',
  `role_name_CN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称中文',
  `update_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authority_role
-- ----------------------------
INSERT INTO `authority_role` VALUES (1, 'user', '普通用户角色', NULL, NULL);
INSERT INTO `authority_role` VALUES (2, 'notoken', '匿名角色', NULL, NULL);
INSERT INTO `authority_role` VALUES (3, 'role1', '角色1', NULL, NULL);

-- ----------------------------
-- Table structure for authority_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `authority_role_menu`;
CREATE TABLE `authority_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int(11) NULL DEFAULT NULL,
  `menu_id` int(11) NULL DEFAULT NULL,
  `update_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 798 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authority_role_menu
-- ----------------------------
INSERT INTO `authority_role_menu` VALUES (1, 1, 1, NULL);
INSERT INTO `authority_role_menu` VALUES (2, 1, 2, NULL);
INSERT INTO `authority_role_menu` VALUES (3, 2, 3, NULL);
INSERT INTO `authority_role_menu` VALUES (4, 1, 4, NULL);
INSERT INTO `authority_role_menu` VALUES (5, 1, 5, NULL);

-- ----------------------------
-- Table structure for authority_user
-- ----------------------------
DROP TABLE IF EXISTS `authority_user`;
CREATE TABLE `authority_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `valid_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '有效截止时间',
  `update_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `remark` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authority_user
-- ----------------------------
INSERT INTO `authority_user` VALUES (1, 'ys', '$2a$10$baCgsjbHS0RZKaLZ03y51.XBhSVgGrtUnQe77yMYSjFmG1RrrgOWm', 'yansu@yansu.com', '13555555555', '2021-10-15 11:51:23', '2021-10-15 11:51:23', NULL, 'yansu');
INSERT INTO `authority_user` VALUES (2, 'yz', '$2a$10$baCgsjbHS0RZKaLZ03y51.XBhSVgGrtUnQe77yMYSjFmG1RrrgOWm', 'yz@yansu.com', '13555555555', '2021-10-15 11:51:23', '2021-10-15 11:51:23', NULL, 'yz');

-- ----------------------------
-- Table structure for authority_user_role
-- ----------------------------
DROP TABLE IF EXISTS `authority_user_role`;
CREATE TABLE `authority_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  `update_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authority_user_role
-- ----------------------------
INSERT INTO `authority_user_role` VALUES (1, 1, 1, NULL);
INSERT INTO `authority_user_role` VALUES (2, 1, 3, NULL);

SET FOREIGN_KEY_CHECKS = 1;
