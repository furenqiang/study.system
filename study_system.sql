/*
 Navicat Premium Data Transfer

 Source Server         : localMysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : study_system

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 22/01/2021 17:20:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `rolename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `roledesc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '增', '增');
INSERT INTO `sys_role` VALUES (2, '删', '删');
INSERT INTO `sys_role` VALUES (3, '改', '改');
INSERT INTO `sys_role` VALUES (4, '查', '查');
INSERT INTO `sys_role` VALUES (5, 'vip', '管理员');
INSERT INTO `sys_role` VALUES (6, 'sysUser', '开发者');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '状态：1-未删除 0-删除',
  `creatTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `creatorId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `creatorName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$BCV14FneWkusM3TEPvM1IeaZzJnfb/l1Y5Yn0nPeANpgbdmbSA6de', 1, '2021-01-22 16:35:10', NULL, NULL);
INSERT INTO `sys_user` VALUES (2, 'Eric', '$2a$10$BCV14FneWkusM3TEPvM1IeaZzJnfb/l1Y5Yn0nPeANpgbdmbSA6de', 1, '2021-01-22 16:35:19', NULL, NULL);
INSERT INTO `sys_user` VALUES (3, 't1', '$2a$10$9Fgu3mYtilvw6w4H.WosO.yXe/R/tKUW6FRDgLDEsoQsZ8sgSHXS6', 1, '2021-01-22 16:50:29', 1, 'admin');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `uid` int(20) NOT NULL COMMENT '用户id',
  `rid` int(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 5);
INSERT INTO `sys_user_role` VALUES (2, 2, 1);
INSERT INTO `sys_user_role` VALUES (3, 2, 2);
INSERT INTO `sys_user_role` VALUES (4, 2, 3);
INSERT INTO `sys_user_role` VALUES (5, 2, 4);
INSERT INTO `sys_user_role` VALUES (6, 2, 6);

SET FOREIGN_KEY_CHECKS = 1;
