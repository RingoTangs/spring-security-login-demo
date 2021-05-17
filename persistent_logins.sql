DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `username` VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `series` VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `token` VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'token',
  `last_used` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上次登录日期',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`series`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT 'RememberMe登录';