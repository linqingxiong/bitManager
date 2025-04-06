CREATE TABLE IF NOT EXISTS lb_product
(
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50),
    title1        VARCHAR(1000),
    title2        VARCHAR(1000),
    desc1         VARCHAR(2000),
    title3        VARCHAR(1000),
    price         VARCHAR(10),
    shipping_cost VARCHAR(10),
    desc2         VARCHAR(2000),
    addr_keyword  VARCHAR(2000),
    cn_desc       VARCHAR(1000),
    cn_title      VARCHAR(2000),
    `size`        VARCHAR(10),
    `update_time` datetime NOT NULL,
    `create_time` datetime NOT NULL
);

CREATE TABLE IF NOT EXISTS lb_pic
(
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id    INTEGER,
    img_url       VARCHAR(1000),
    `create_time` datetime NOT NULL
);

CREATE TABLE IF NOT EXISTS lb_operation_log
(
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id       INTEGER,
    operation_time   datetime NOT NULL,
    operation_type   VARCHAR(255),
    browser_id       VARCHAR(50),
    screen_shot_url  VARCHAR(255),
    operation_param  TEXT,
    operation_result TEXT,
    error_message    TEXT
);
CREATE TABLE IF NOT EXISTS setting
(
    `setting_key`   VARCHAR(255) PRIMARY KEY NOT NULL,
    `setting_value` VARCHAR(1000)            NOT NULL,
    type            VARCHAR(20),
    description     VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS put_online_task
(
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id    INTEGER,
    browser_id    VARCHAR(50),
    proxy_ip      VARCHAR(20),
    schedule_time BIGINT   NOT NULL, -- 预定执行时间戳
    status        VARCHAR(20) DEFAULT '准备',
    `update_time` datetime NOT NULL,
    `create_time` datetime NOT NULL,
    retry_count   INTEGER,
    version       INTEGER
);
CREATE TABLE IF NOT EXISTS `proxy_config`
(
    `id`              INTEGER PRIMARY KEY AUTOINCREMENT,-- '代理配置ID',
    `config_name`     VARCHAR(100),                     -- '配置名称',
    `host`            VARCHAR(255) NOT NULL,            -- '代理主机',
    `port`            INT          NOT NULL,            -- '代理端口',
    `proxy_type`      VARCHAR(50)  NOT NULL,            -- '代理类型',
    `proxy_user_name` VARCHAR(255),                     -- '代理用户名（加密存储）',
    `proxy_password`  VARCHAR(255),                     -- '代理密码（加密存储）',
    `enabled`         TINYINT(1) DEFAULT 1,             -- '是否启用',
    `description`     VARCHAR(500),                     -- '配置描述',
    `proxy_info`      TEXT,                             -- '配置描述',
    `create_time`     DATETIME     NOT NULL,            -- '创建时间',
    `update_time`     DATETIME     NOT NULL             -- '更新时间'
);
