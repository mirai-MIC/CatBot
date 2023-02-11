/*
 Navicat Premium Data Transfer

 Source Server         : localsql
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : signsql

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 12/02/2023 00:13:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for loadday
-- ----------------------------
DROP TABLE IF EXISTS `loadday`;
CREATE TABLE `loadday`  (
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sumplus
-- ----------------------------
DROP TABLE IF EXISTS `sumplus`;
CREATE TABLE `sumplus`  (
  `id` bigint NOT NULL,
  `sum` bigint NOT NULL,
  `daytime` date NOT NULL,
  `sumday` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
