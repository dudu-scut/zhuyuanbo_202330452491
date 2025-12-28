-- 创建用户操作日志表
CREATE TABLE user_operation_log
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    operation_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operation_content TEXT         NOT NULL,
    ip_address        VARCHAR(45)  NOT NULL,
    account           VARCHAR(100) NOT NULL
);

-- 创建浏览记录表
CREATE TABLE IF NOT EXISTS browse_record
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录 ID',
    goods_id      BIGINT NOT NULL COMMENT '商品 ID',
    user_id       BIGINT NOT NULL COMMENT '用户 ID',
    stay_duration BIGINT NOT NULL COMMENT '停留时长（毫秒）',
    browse_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='浏览记录表';