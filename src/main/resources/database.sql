
-- Dumping database structure for chain
CREATE DATABASE IF NOT EXISTS `chain` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `chain`;

-- Dumping structure for table chain.trade_info
CREATE TABLE IF NOT EXISTS `trade_info` (
  `hash` varchar(70) NOT NULL COMMENT '交易HASH',
  `block_number` bigint(32) DEFAULT NULL,
  `gas` varchar(64) DEFAULT NULL,
  `gas_price` varchar(64) DEFAULT NULL,
  `t_value` varchar(64) DEFAULT NULL,
  `nonce` bigint(32) DEFAULT NULL,
  `from_account` varchar(50) DEFAULT NULL,
  `to_account` varchar(50) DEFAULT NULL,
  `block_hash` varchar(70) DEFAULT NULL,
  `transaction_index` bigint(32) DEFAULT NULL,
  `public_key` varchar(70) DEFAULT NULL,
  `r` varchar(70) DEFAULT NULL,
  `s` varchar(70) DEFAULT NULL,
  `v` varchar(70) DEFAULT NULL,
  `t_input` text,
  `trade_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`hash`),
  KEY `idx_trade_time` (`trade_time`),
  KEY `idx_trade_from` (`from_account`),
  KEY `idx_trade_to` (`to_account`),
  KEY `idx_trade_number` (`block_number`),
  KEY `idx_trade_nonce` (`nonce`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存放区块链中的全部交易详细信息';


-- Dumping database structure for cms
CREATE DATABASE IF NOT EXISTS `cms` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `cms`;

-- Dumping structure for table cms.hpb_data_dic
CREATE TABLE IF NOT EXISTS `hpb_data_dic` (
  `id` bigint(32) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_type_no` int(11) unsigned NOT NULL COMMENT '数据类型编号',
  `data_type_name` varchar(120) DEFAULT NULL COMMENT '数据类型名称',
  `data_no` varchar(60) NOT NULL COMMENT '数据字典编号',
  `data_name` varchar(500) DEFAULT NULL COMMENT '数据字典名称',
  `state` varchar(1) NOT NULL DEFAULT '1' COMMENT '状态：1-生效；0-失效',
  `data_no_len` int(11) DEFAULT NULL COMMENT '数据编号长度',
  `limit_flag` varchar(1) DEFAULT NULL COMMENT '是否有上下限 0-否 1-是',
  `high_limit` varchar(20) DEFAULT NULL COMMENT '上限',
  `low_limit` varchar(20) DEFAULT NULL COMMENT '下限',
  `effect_date` bigint(32) DEFAULT NULL COMMENT '生效日期',
  `expire_date` bigint(32) DEFAULT NULL COMMENT '失效日期',
  `create_time` bigint(32) DEFAULT NULL COMMENT '创建时间',
  `show_seq` int(6) DEFAULT '0' COMMENT '显示顺序',
  `update_time` bigint(32) DEFAULT NULL COMMENT '创建时间',
  `user_id` int(10) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='数据字典表';

-- Dumping data for table cms.hpb_data_dic: ~9 rows (大约)
/*!40000 ALTER TABLE `hpb_data_dic` DISABLE KEYS */;
INSERT INTO `hpb_data_dic` (`id`, `data_type_no`, `data_type_name`, `data_no`, `data_name`, `state`, `data_no_len`, `limit_flag`, `high_limit`, `low_limit`, `effect_date`, `expire_date`, `create_time`, `show_seq`, `update_time`, `user_id`) VALUES
	(1, 111, 'HPB配置', 'gasLimit', '500000', '1', NULL, NULL, NULL, NULL, NULL, NULL, 1533547021696, 0, 1533547171417, 1),
	(2, 111, 'HPB配置', 'gasPrice', '18000000000', '1', NULL, NULL, NULL, NULL, NULL, NULL, 1533547021696, 0, 1533547171417, 1);
	

-- Dumping database structure for token
CREATE DATABASE IF NOT EXISTS `token` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `token`;

-- Dumping structure for table token.contract_erc_standard_info
CREATE TABLE IF NOT EXISTS `contract_erc_standard_info` (
  `id` varchar(32) NOT NULL,
  `token_symbol` varchar(128) DEFAULT NULL ,
  `token_symbol_image_url` varchar(128) DEFAULT NULL  ,
  `token_name` varchar(128) DEFAULT NULL  ,
  `decimals` bigint(32) DEFAULT NULL  ,
  `deploy_tx_hash` varchar(128) DEFAULT NULL,
  `contract_creater` varchar(128) DEFAULT NULL,
  `contract_address` varchar(128) DEFAULT NULL  ,
  `token_total_supply` bigint(32) DEFAULT NULL  ,
  `contract_type` varchar(128) DEFAULT NULL  ,
  `verified_status` varchar(128) DEFAULT NULL  ,
  `price` varchar(45) DEFAULT NULL  ,
  `change_rate` varchar(45) DEFAULT NULL  ,
  `volume_24h` varchar(45) DEFAULT NULL  ,
  `market_cap` decimal(32,8) DEFAULT NULL  ,
  `holders` int(11) DEFAULT NULL  ,
  `transfers_num` int(11) DEFAULT NULL  ,
  `status` varchar(32) DEFAULT NULL  ,
  `create_timestamp` datetime DEFAULT NULL ,
  `update_timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_tx_hash` (`deploy_tx_hash`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table token.tx_transfer_record
CREATE TABLE IF NOT EXISTS `tx_transfer_record` (
  `tx_hash` varchar(66) DEFAULT NULL,
  `block_hash` varchar(66) DEFAULT NULL,
  `block_number` bigint(22) DEFAULT NULL,
  `block_timestamp` bigint(20) DEFAULT NULL,
  `contract_address` varchar(66) DEFAULT NULL,
  `from_address` varchar(66) DEFAULT NULL,
  `to_address` varchar(66) DEFAULT NULL,
  `quantity` varchar(32) DEFAULT NULL,
  `token_type` varchar(64) DEFAULT NULL,
  `log_index` bigint(12) DEFAULT NULL,
  `token_id` bigint(12) DEFAULT NULL,
  `create_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  KEY `tx_idx` (`tx_hash`) USING BTREE,
  KEY `contract_address_idx` (`contract_address`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- Dumping database structure for website
CREATE DATABASE IF NOT EXISTS `website` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `website`;

-- Dumping structure for table website.coin_config
CREATE TABLE IF NOT EXISTS `coin_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coin_symbol` varchar(32) DEFAULT NULL,
  `coin_full_name` varchar(64) DEFAULT NULL,
  `icon_url` varchar(200) DEFAULT NULL,
  `coin_precision` varchar(18) DEFAULT NULL,
  `contract_addr` varchar(64) DEFAULT NULL,
  `coin_type` varchar(1) DEFAULT NULL,
  `order_num` int(11) DEFAULT NULL,
  `price_from` varchar(1) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  `is_show` varchar(1) DEFAULT NULL,
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `gmt_modify` datetime DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- Dumping structure for table website.hpb_instant_price
CREATE TABLE IF NOT EXISTS `hpb_instant_price` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `coin_symbol` varchar(60) DEFAULT NULL COMMENT '代币简称',
  `cny_price` varchar(60) DEFAULT NULL COMMENT '人民币价格',
  `usd_price` varchar(60) DEFAULT NULL COMMENT '美元价格',
  `change_percent` varchar(60) DEFAULT NULL COMMENT 'change_percent  涨跌幅百分比',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最新更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='代币价格表';

-- Dumping data for table website.hpb_instant_price: ~2 rows (大约)
/*!40000 ALTER TABLE `hpb_instant_price` DISABLE KEYS */;
INSERT INTO `hpb_instant_price` (`id`, `coin_symbol`, `cny_price`, `usd_price`, `change_percent`, `update_time`) VALUES
	(1, 'HPB', '1.3059', '0.1865', '+30.48%', '2020-02-05 13:00:00'),
	(2, 'BPT', '0.0092', '0.0013', '-14.35%', '2020-02-05 13:00:00');
/*!40000 ALTER TABLE `hpb_instant_price` ENABLE KEYS */;

-- Dumping structure for table website.issue_vote
CREATE TABLE IF NOT EXISTS `issue_vote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue_no` varchar(32) DEFAULT NULL,
  `vote_status` char(1) DEFAULT NULL,
  `name_zh` varchar(254) DEFAULT NULL,
  `name_en` varchar(254) DEFAULT NULL,
  `desc_zh` varchar(254) DEFAULT NULL,
  `desc_en` varchar(254) DEFAULT NULL,
  `value1_zh` varchar(254) DEFAULT NULL,
  `value2_zh` varchar(254) DEFAULT NULL,
  `value1_en` varchar(254) DEFAULT NULL,
  `value2_en` varchar(254) DEFAULT NULL,
  `floor_num` decimal(65,0) DEFAULT NULL,
  `begin_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `is_vaild` char(254) DEFAULT NULL,
  `gmt_create` timestamp NULL DEFAULT NULL,
  `gmt_modify` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- Dumping structure for table website.issue_vote_detail
CREATE TABLE IF NOT EXISTS `issue_vote_detail` (
  `id` bigint(20) DEFAULT NULL,
  `issue_no` varchar(254) DEFAULT NULL,
  `option_key` varchar(254) DEFAULT NULL,
  `option_menu` varchar(254) DEFAULT NULL,
  `vote_addr` varchar(254) DEFAULT NULL,
  `vote_num` double DEFAULT NULL,
  `gmt_create` date DEFAULT NULL,
  `gmt_modify` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table website.issue_vote_detail: ~0 rows (大约)
/*!40000 ALTER TABLE `issue_vote_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `issue_vote_detail` ENABLE KEYS */;

-- Dumping structure for table website.issue_vote_result
CREATE TABLE IF NOT EXISTS `issue_vote_result` (
  `id` bigint(20) DEFAULT NULL,
  `issue_no` varchar(254) DEFAULT NULL,
  `option_key` varchar(254) DEFAULT NULL,
  `option_valu` varchar(254) DEFAULT NULL,
  `vote_num` double DEFAULT NULL,
  `vote_ratio` double DEFAULT NULL,
  `gmt_create` date DEFAULT NULL,
  `gmt_modify` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table website.issue_vote_result: ~0 rows (大约)
/*!40000 ALTER TABLE `issue_vote_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `issue_vote_result` ENABLE KEYS */;

-- Dumping structure for table website.red_packet
CREATE TABLE IF NOT EXISTS `red_packet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `red_packet_no` varchar(42) NOT NULL COMMENT '红包编号',
  `is_over` char(1) DEFAULT NULL COMMENT '是否结束：0-未开始，1-进行中，2-结束',
  `tx_hash` varchar(100) DEFAULT NULL COMMENT '交易HASH',
  `status` char(1) DEFAULT NULL COMMENT '状态：1-成功，0-失败，2-确认中',
  `coin_symbol` varchar(10) NOT NULL COMMENT '币种',
  `from_addr` varchar(42) NOT NULL COMMENT '发红包地址',
  `total_num` int(11) NOT NULL COMMENT '红包总数',
  `red_packet_type` char(1) NOT NULL COMMENT '红包类型：1-普通 2-拼手气',
  `enter_type` char(1) NOT NULL COMMENT '红包入口：1-多渠道',
  `total_coin` varchar(100) DEFAULT NULL COMMENT '不含旷工费',
  `total_cny` decimal(5,2) DEFAULT NULL COMMENT 'CNY总金额',
  `total_usd` decimal(5,2) DEFAULT NULL COMMENT 'usd总金额',
  `cny_price` decimal(5,4) DEFAULT NULL COMMENT 'CNY实时价格',
  `miner_fee` varchar(100) DEFAULT NULL COMMENT '打包费用+（10个红包每次转账的交易费用）',
  `is_over_time` char(1) DEFAULT NULL COMMENT '是否过期：1-是，0-否',
  `title` varchar(50) DEFAULT NULL COMMENT '红包主题',
  `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '红包开始时间',
  `end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '红包结束时间',
  `duration_time` bigint(20) DEFAULT '0' COMMENT '红包淘完用时：秒（存在退款没值）',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='红包表';


-- Dumping structure for table website.red_packet_config
CREATE TABLE IF NOT EXISTS `red_packet_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `min_num` int(11) NOT NULL COMMENT '红包最小数量',
  `max_num` int(11) NOT NULL COMMENT '红包最大数量',
  `min_per_coin` decimal(5,4) NOT NULL COMMENT '单个最小代币',
  `max_per_coin` decimal(7,4) NOT NULL COMMENT '单个最大代币',
  `time` int(11) NOT NULL COMMENT '有效时长(单位:小时)',
  `multiple` int(11) NOT NULL COMMENT '钥匙倍数',
  `rate` decimal(3,2) DEFAULT NULL COMMENT '得奖概率(0-1)',
  `is_vaild` char(1) NOT NULL COMMENT '是否有效：1-有效 0-无效',
  `enter_type` char(1) NOT NULL COMMENT '1-红包，2-官方',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='红包配置表';

-- Dumping data for table website.red_packet_config: ~0 rows (大约)
/*!40000 ALTER TABLE `red_packet_config` DISABLE KEYS */;
INSERT INTO `red_packet_config` (`id`, `min_num`, `max_num`, `min_per_coin`, `max_per_coin`, `time`, `multiple`, `rate`, `is_vaild`, `enter_type`, `gmt_create`, `gmt_modify`) VALUES
	(1, 1, 10, 0.0100, 100.0000, 24, 10, NULL, '1', '1', '2019-07-04 10:39:21', '2019-07-04 10:39:21');
/*!40000 ALTER TABLE `red_packet_config` ENABLE KEYS */;

-- Dumping structure for table website.red_packet_detail
CREATE TABLE IF NOT EXISTS `red_packet_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `red_packet_no` varchar(42) NOT NULL COMMENT '红包编号',
  `status` char(1) DEFAULT NULL COMMENT '状态：1-成功，0-失败，2-确认中',
  `trade_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '交易时间',
  `token_id` bigint(20) DEFAULT NULL COMMENT '资产编号',
  `from_addr` varchar(42) NOT NULL COMMENT '发红包地址',
  `to_addr` varchar(42) NOT NULL COMMENT '领红包地址',
  `packet_key` varchar(42) NOT NULL COMMENT '钥匙',
  `coin_value` varchar(100) DEFAULT NULL COMMENT '代币金额',
  `tx_hash` varchar(100) DEFAULT NULL COMMENT '交易HASH',
  `max_flag` char(1) DEFAULT NULL COMMENT '1-手气最佳',
  `red_packet_type` char(1) NOT NULL COMMENT '红包类型：1-普通 2-拼手气',
  `title` varchar(50) DEFAULT NULL COMMENT '红包主题',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='红包明细表';

-- Dumping structure for table website.red_packet_key
CREATE TABLE IF NOT EXISTS `red_packet_key` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `red_packet_no` varchar(42) NOT NULL COMMENT '红包编号',
  `is_vaild` char(1) NOT NULL COMMENT '是否有效：0-未使用，1-已使用',
  `packet_key` varchar(42) NOT NULL COMMENT '钥匙',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='红包钥匙表';

-- Dumping structure for table website.red_packet_refund
CREATE TABLE IF NOT EXISTS `red_packet_refund` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `red_packet_no` varchar(42) NOT NULL COMMENT '红包编号',
  `token_id` bigint(20) DEFAULT NULL COMMENT '资产编号',
  `from_addr` varchar(42) NOT NULL COMMENT '转出地址',
  `to_addr` varchar(42) NOT NULL COMMENT '转入地址',
  `coin_value` varchar(100) DEFAULT NULL COMMENT '金额（不含旷工费）',
  `hash` varchar(100) DEFAULT NULL COMMENT '交易HASH',
  `status` char(1) DEFAULT NULL COMMENT '状态：1-成功，0-失败，2-确认中',
  `trade_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '交易时间',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='红包退款表';

-- Dumping structure for table website.red_packet_send
CREATE TABLE IF NOT EXISTS `red_packet_send` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `red_packet_no` varchar(42) NOT NULL COMMENT '红包编号',
  `token_id` bigint(20) DEFAULT NULL COMMENT '资产编号',
  `token_value` varchar(100) DEFAULT NULL COMMENT '红包金额（不含矿工费用）',
  `from_addr` varchar(42) NOT NULL COMMENT '发红包地址',
  `hash` varchar(100) DEFAULT NULL COMMENT '交易HASH',
  `status` char(1) DEFAULT NULL COMMENT '状态：1-成功，0-失败',
  `is_used` char(1) DEFAULT '0' COMMENT '状态：0-未使用，1-已使用',
  `type` char(1) NOT NULL COMMENT '红包类型：1-普通 2-拼手气',
  `max_flag` char(1) DEFAULT NULL COMMENT '1-手气最佳',
  `title` varchar(50) DEFAULT NULL COMMENT '红包主题',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='发红包明细表';

-- Dumping structure for table website.red_packet_use
CREATE TABLE IF NOT EXISTS `red_packet_use` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `red_packet_no` varchar(42) NOT NULL COMMENT '红包编号',
  `total_num` int(11) NOT NULL COMMENT '红包数量',
  `used_num` int(11) DEFAULT '0' COMMENT '已领红包数量',
  `refund_num` int(11) DEFAULT '0' COMMENT '退红包数量',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='红包领取数量表';
