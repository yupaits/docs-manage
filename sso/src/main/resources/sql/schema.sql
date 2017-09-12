DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL COMMENT '用户名',
  `email` VARCHAR(100) DEFAULT '' COMMENT '邮箱',
  `salt` VARCHAR(36) DEFAULT '' COMMENT '盐',
  `password` VARCHAR(64) DEFAULT '' COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户表';
ALTER TABLE `user` ADD UNIQUE INDEX uk_username(`username`);
ALTER TABLE `user` ADD INDEX idx_email(`email`);

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(100) NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '角色表';
ALTER TABLE `role` ADD UNIQUE INDEX uk_role_name(`role_name`);

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` INT NOT NULL COMMENT '用户ID',
  `role_id` INT NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户-角色表';
