INSERT INTO `user`(`username`, `email`, `salt`, `password`) VALUES
('admin', 'qq@qq.com', '21232f29-7a57-35a7-8389-4a0e4a801fc3', '24bf6a93837a98dfd668d5641fb89748c75b0ab030bdb161fdd516835e4627d7'),
('user', '163@163.com', 'ee11cbb1-9052-340b-87aa-c0ca060c23ee', '04557b7d17320f2de2b3e34165a7bb29f06bd30add54d30ca352e0f99c9a0b03')
;

INSERT INTO `role`(`role_name`) VALUES
('admin'),
('user')
;

INSERT INTO `user_role`(`user_id`, `role_id`) VALUES
(1, 1),(1, 2),
(2, 2)
;
