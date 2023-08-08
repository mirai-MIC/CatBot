CREATE TABLE `av_detail`
(
    `id`              int          NOT NULL AUTO_INCREMENT,
    `av_Num`          varchar(100) NOT NULL COMMENT 'AV番号',
    `actors`          varchar(255)      DEFAULT NULL COMMENT '影片演员',
    `cover_Url`       varchar(255)      DEFAULT NULL COMMENT '视频封面地址',
    `magnet_Link`     varchar(5000)     DEFAULT NULL COMMENT '视频磁力链接',
    `online_play_Url` varchar(500)      DEFAULT NULL COMMENT '在线播放地址',
    `description`     varchar(1000)     DEFAULT NULL COMMENT '作品描述',
    `duration`        int unsigned      DEFAULT '0' COMMENT '作品时长',
    `categories`      varchar(1000)     DEFAULT NULL COMMENT '作品类别',
    `release_Date`    timestamp    NULL DEFAULT NULL COMMENT '发布时间',
    `magnet_link_hd`  varchar(5000)     DEFAULT NULL COMMENT '高清磁力',
    `magnet_link_sub` varchar(2000)     DEFAULT NULL COMMENT '字幕磁力',
    `title`           varchar(1000)     DEFAULT NULL COMMENT '影片标题',
    PRIMARY KEY (`id`),
    UNIQUE KEY `pk_av_num` (`av_Num`) COMMENT '番号唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `av_preview`
(
    `id`          int          NOT NULL AUTO_INCREMENT,
    `av_num`      varchar(100) NOT NULL COMMENT 'AV番号',
    `preview_url` varchar(255) DEFAULT NULL COMMENT '预览图地址',
    PRIMARY KEY (`id`),
    KEY `pk_av_num_preview` (`av_num`) USING BTREE COMMENT 'AV番号'
) ENGINE = InnoDB
  AUTO_INCREMENT = 33
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;