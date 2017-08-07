INSERT INTO `user`(`username`, `password`) VALUES ('admin', 'f143549d86bc57c801586a3c27bc65394809b68430ac387de5a79619bc5a6085');

INSERT INTO `role`(`role_name`) VALUES ('ROLE_ADMIN');

INSERT INTO `user_role`(`user_id`, `role_id`) VALUES (1, 1);
