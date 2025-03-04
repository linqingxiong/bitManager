CREATE TABLE IF NOT EXISTS lb_product
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255),
    title VARCHAR(1000),
    key   VARCHAR(2000)
);

CREATE TABLE IF NOT EXISTS lb_pic
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    img_url    VARCHAR(1000)
);
