#用户表
DROP TABLE IF EXISTS `blog_system_user`;
CREATE TABLE `blog_system_user` (
     `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
     `username` varchar(20) NOT NULL COMMENT '用户名',
     `password` varchar(20) NOT NULL COMMENT '密码',
     `user_nickname` varchar(20) NOT NULL COMMENT '用户昵称',
     `create_time` datetime NOT NULL COMMENT '创建时间',
     `update_time` datetime NOT NULL COMMENT '更新时间',
     PRIMARY key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;