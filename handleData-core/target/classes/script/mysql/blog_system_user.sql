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

DROP TABLE IF EXISTS `blog_system_index_target_data`;
CREATE TABLE `blog_system_index_target_data` (
    `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `index` varchar(20) NOT NULL COMMENT '索引',
    `index_chinese_name` varchar(20) NOT NULL COMMENT '索引中文名',
    `before_treatment` varchar(20) NOT NULL COMMENT '治理前',
    `after_treatment` varchar(20) NOT NULL COMMENT '治理后',
    `governance_failure` varchar(20) NOT NULL COMMENT '治理失败',
    `warehousing_succeeded` datetime NOT NULL COMMENT '入库成功',
    `warehousing_failed` datetime NOT NULL COMMENT '入库成功',
    `removal_quantity` datetime NOT NULL COMMENT '去重数量',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;