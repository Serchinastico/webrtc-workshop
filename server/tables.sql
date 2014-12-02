CREATE TABLE IF NOT EXISTS `sessions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `time` bigint(20) unsigned NOT NULL,
  `name` varchar(64) NOT NULL,
  `last_request` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `requests` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `from_user` varchar(64) NOT NULL,
  `to_user` varchar(64) NOT NULL,
  `type` varchar(16) NOT NULL,
  `request` varchar(5000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;