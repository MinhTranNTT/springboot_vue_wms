/*
Navicat MySQL Data Transfer

Source Server         : huawei_yunserver
Source Server Version : 50740
Source Host           : 139.9.212.105:3306
Source Database       : wms

Target Server Type    : MYSQL
Target Server Version : 50740
File Encoding         : 65001

Date: 2023-01-04 10:25:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '货名',
  `storage` int(11) NOT NULL COMMENT '仓库',
  `goodsType` int(11) NOT NULL COMMENT '分类',
  `count` int(11) DEFAULT NULL COMMENT '数量',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('1', '方射灯', '1', '1', '51', '', '0');
INSERT INTO `goods` VALUES ('2', 'VCD机', '1', '1', '13', '', '0');
INSERT INTO `goods` VALUES ('3', '无线麦克风接收器', '1', '1', '226', '', '0');
INSERT INTO `goods` VALUES ('4', '三角支架', '1', '1', '5', '', '0');
INSERT INTO `goods` VALUES ('5', '四角底座', '1', '1', '125', '', '0');
INSERT INTO `goods` VALUES ('6', '声控机', '1', '1', '1', '', '0');
INSERT INTO `goods` VALUES ('7', '小鼓风机', '1', '1', '3', '', '0');
INSERT INTO `goods` VALUES ('8', '大鼓风机', '1', '1', '3', '', '0');
INSERT INTO `goods` VALUES ('9', '圆射灯', '1', '1', '7', '', '0');
INSERT INTO `goods` VALUES ('10', '吹泡泡机', '1', '1', '5', '', '0');
INSERT INTO `goods` VALUES ('11', '彩色三角挂旗', '1', '2', '1', '箱', '0');
INSERT INTO `goods` VALUES ('12', '气球塑料管', '1', '2', '2', '麻袋', '0');
INSERT INTO `goods` VALUES ('13', '电灯泡', '1', '2', '2', '', '0');
INSERT INTO `goods` VALUES ('14', '圣诞帽子', '1', '2', '160', '圣诞节活动', '0');
INSERT INTO `goods` VALUES ('15', '铅笔芯', '2', '3', '20', '', '0');
INSERT INTO `goods` VALUES ('16', '水笔', '2', '3', '5', '', '0');
INSERT INTO `goods` VALUES ('17', '墨水', '2', '3', '2', '', '0');
INSERT INTO `goods` VALUES ('18', '双氧水', '2', '4', '1', '', '0');
INSERT INTO `goods` VALUES ('19', '葡萄糖', '2', '4', '4', '', '0');
INSERT INTO `goods` VALUES ('20', '纱布', '2', '4', '2', '', '0');
INSERT INTO `goods` VALUES ('21', '跳绳', '2', '6', '5', '', '0');
INSERT INTO `goods` VALUES ('22', '毽子', '2', '6', '2', '', '0');
INSERT INTO `goods` VALUES ('26', '百事可乐', '2', '1', '2', null, '0');

-- ----------------------------
-- Table structure for goodstype
-- ----------------------------
DROP TABLE IF EXISTS `goodstype`;
CREATE TABLE `goodstype` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '分类名',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goodstype
-- ----------------------------
INSERT INTO `goodstype` VALUES ('1', '音响设备', '各类媒体娱乐设备，外拍、大型活动必须使用');
INSERT INTO `goodstype` VALUES ('2', '活动用品', '各类团建活动举办所需用品');
INSERT INTO `goodstype` VALUES ('3', '文具', '');
INSERT INTO `goodstype` VALUES ('4', '医用品', '');
INSERT INTO `goodstype` VALUES ('5', '日常用品', '');
INSERT INTO `goodstype` VALUES ('6', '杂物', '');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL COMMENT '主键',
  `menuCode` varchar(8) DEFAULT NULL COMMENT '菜单编码',
  `menuName` varchar(16) DEFAULT NULL COMMENT '菜单名字',
  `menuLevel` varchar(2) DEFAULT NULL COMMENT '菜单级别',
  `menuParentCode` varchar(8) DEFAULT NULL COMMENT '菜单的父code',
  `menuClick` varchar(16) DEFAULT NULL COMMENT '点击触发的函数',
  `menuRight` varchar(8) DEFAULT NULL COMMENT '权限 0超级管理员，1表示管理员，2表示普通用户，可以用逗号组合使用',
  `menuComponent` varchar(200) DEFAULT NULL,
  `menuIcon` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '001', '管理员管理', '1', '', 'Admin', '0', 'admin/AdminManage.vue', 'el-icon-s-custom');
INSERT INTO `menu` VALUES ('2', '002', '用户管理', '1', '', 'User', '0,1', 'user/UserManage.vue', 'el-icon-user-solid');
INSERT INTO `menu` VALUES ('3', '003', '仓库管理', '1', '', 'Storage', '0,1', 'storage/StorageManage', 'el-icon-office-building');
INSERT INTO `menu` VALUES ('4', '004', '物品分类管理', '1', '', 'Goodstype', '0,1', 'goodstype/GoodstypeManage', 'el-icon-menu');
INSERT INTO `menu` VALUES ('5', '005', '物品管理 ', '1', null, 'Goods', '0,1,2', 'goods/GoodsManage', 'el-icon-s-management');
INSERT INTO `menu` VALUES ('6', '006', '记录管理', '1', null, 'Record', '0,1,2', 'record/RecordManage', 'el-icon-s-order');

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `goods` int(11) NOT NULL COMMENT '货品id',
  `userId` int(11) DEFAULT NULL COMMENT '取货人/补货人',
  `admin_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `count` int(11) DEFAULT NULL COMMENT '数量',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES ('1', '13', '5', '1', '2', '2022-12-19 23:31:38', '');
INSERT INTO `record` VALUES ('2', '14', '4', '1', '160', '2022-12-19 23:33:12', '');
INSERT INTO `record` VALUES ('3', '15', '5', '1', '20', '2022-12-19 23:38:26', '');
INSERT INTO `record` VALUES ('4', '16', '4', '1', '5', '2022-12-19 23:38:59', '');
INSERT INTO `record` VALUES ('5', '17', '6', '1', '2', '2022-12-19 23:40:30', '');
INSERT INTO `record` VALUES ('6', '18', '5', '1', '1', '2022-12-19 23:41:28', '');
INSERT INTO `record` VALUES ('7', '19', '6', '1', '4', '2022-12-19 23:41:38', '');
INSERT INTO `record` VALUES ('8', '20', '6', '1', '2', '2022-12-19 23:41:48', '');
INSERT INTO `record` VALUES ('9', '21', '4', '1', '5', '2022-12-19 23:44:58', '');
INSERT INTO `record` VALUES ('10', '22', '5', '1', '2', '2022-12-19 23:45:04', '');
INSERT INTO `record` VALUES ('11', '2', '4', '1', '123', '2022-12-22 18:01:18', '');
INSERT INTO `record` VALUES ('12', '2', '4', '1', '123', '2022-12-22 18:01:18', '');
INSERT INTO `record` VALUES ('13', '1', '4', '1', '123', '2022-12-22 18:01:29', '');
INSERT INTO `record` VALUES ('14', '1', '4', '1', '-100', '2022-12-22 18:02:28', '');
INSERT INTO `record` VALUES ('15', '1', '4', '1', '100', '2022-12-22 18:03:04', '');
INSERT INTO `record` VALUES ('16', '3', '4', '1', '12', '2022-12-22 18:59:20', '');
INSERT INTO `record` VALUES ('17', '1', '5', '1', '12', '2022-12-22 18:59:44', '');
INSERT INTO `record` VALUES ('18', '5', '6', '1', '123', '2022-12-22 19:03:37', '');
INSERT INTO `record` VALUES ('19', '3', '5', '1', '10', '2022-12-22 19:04:10', '');
INSERT INTO `record` VALUES ('20', '1', '4', '1', '10', '2022-12-22 19:06:25', '');
INSERT INTO `record` VALUES ('21', '2', '4', '1', '12', '2022-12-22 19:11:39', '');
INSERT INTO `record` VALUES ('22', '2', '4', '1', '12', '2022-12-22 19:13:43', '');
INSERT INTO `record` VALUES ('23', '2', '5', '1', '12', '2022-12-22 19:16:35', '');
INSERT INTO `record` VALUES ('24', '4', '4', '1', '3', '2022-12-22 19:21:30', '');
INSERT INTO `record` VALUES ('25', '1', '4', '1', '12', '2022-12-22 19:24:31', '');
INSERT INTO `record` VALUES ('26', '3', '5', '1', '-12', '2022-12-22 19:24:56', '');
INSERT INTO `record` VALUES ('27', '3', '4', '1', '12', '2022-12-23 10:58:59', '123');
INSERT INTO `record` VALUES ('28', '3', '6', '1', '12', '2023-01-01 00:05:45', '');

-- ----------------------------
-- Table structure for storage
-- ----------------------------
DROP TABLE IF EXISTS `storage`;
CREATE TABLE `storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '仓库名',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of storage
-- ----------------------------
INSERT INTO `storage` VALUES ('1', '企业部仓库', '报损物品清单（鼓风机*1、射灯*1）', '0');
INSERT INTO `storage` VALUES ('2', '学生会仓库', '校学生会各类日常物品仓库', '0');
INSERT INTO `storage` VALUES ('7', 'test123', 'sd123', '1');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `no` varchar(20) DEFAULT NULL COMMENT '账号',
  `name` varchar(100) NOT NULL COMMENT '名字',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `sex` int(11) DEFAULT NULL COMMENT '性别',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `role_id` int(11) DEFAULT NULL COMMENT '⻆⾊ 0超级管理员，1管理员，2普通账号',
  `isValid` varchar(4) DEFAULT 'Y' COMMENT '是否有效，Y有效，其他⽆效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '超级管理员', '$2a$10$hKhAyATu./75go7JYbR9COLUaNHz.tm/sm37TGz1IeALTmZ9ahHpK', '18', '1', '111', '0', 'Y');
INSERT INTO `user` VALUES ('2', 'zhangsan', '张三', '$2a$10$/UcDtiYptkuH9mEojImAk.ZXofXS3Yi.LBnUknMehw8ArjAWVNH/.', '25', '1', '15015947765', '1', 'Y');
INSERT INTO `user` VALUES ('3', 'lisi', '李四', '$2a$10$/3k83SsV.e/AAutscv6ScO8V4V29x0bzNzZ8G2YxEKWkc18OK7q2y', '26', '0', '13645698875', '1', 'Y');
INSERT INTO `user` VALUES ('4', 'wangwu', '王五', '$2a$10$ZjYQ68IHQcuhQtaXUtPEiu6va9rrhTSccp2UwIOX4gOcaHfDY7bnO', '33', '1', '16815698845', '2', 'Y');
INSERT INTO `user` VALUES ('5', 'wangmeimei', '王美美', '$2a$10$E6R0qxZL67XuzfiM5MIvteyfZ7fWjQbeCETksBmhocnVhu0.UCb5m', '27', '0', '13659864452', '2', 'Y');
INSERT INTO `user` VALUES ('6', 'liming', '李明', '$2a$10$08KOa/VIgZtBGm4YK4iRzuuMjdKF3.ZUX.1Nrojr7wGj4TmwgrElG', '34', '1', '15046589974', '2', 'Y');
