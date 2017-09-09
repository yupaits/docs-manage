INSERT INTO `user`(`username`, `password`) VALUES
('admin', 'f71de501f0ff4ad3b7a8584a846cd911085424c6ecd09b588164c35152b4e264'),
('user', 'c0f257a0c2de33679581ce2561dfbae58d791949bc710a1b585e1c86fb23e1cc')
;

INSERT INTO `role`(`role_name`) VALUES
('ADMIN'),
('USER')
;

INSERT INTO `user_role`(`user_id`, `role_id`) VALUES
(1, 1),(1, 2),
(2, 2)
;
