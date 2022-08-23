DROP TABLE IF EXISTS `blog_system_country_data`;
CREATE TABLE `blog_system_country_data` (
    `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `country` varchar(20) NOT NULL COMMENT '国家名',
    PRIMARY key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;