DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
`username` VARCHAR(48) NOT NULL COMMENT '用户名',
`password` VARCHAR(256) NOT NULL COMMENT '密码',
`mobile` VARCHAR(11) DEFAULT NULL COMMENT '手机号',
`create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册日期',
`update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
PRIMARY KEY (`username`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;