INSERT INTO setting (`setting_key`, `setting_value`, `type`, description)
SELECT 'bt_browser', '', '文件', '比特浏览器位置'
WHERE NOT EXISTS (SELECT 1
                  FROM setting
                  WHERE `setting_key` = 'bt_browser');

INSERT INTO setting (`setting_key`, `setting_value`, `type`, description)
SELECT 'local_product_gallery', '', '文件', '产品库'
WHERE NOT EXISTS (SELECT 1
                  FROM setting
                  WHERE `setting_key` = 'local_product_gallery');


INSERT INTO setting (`setting_key`, `setting_value`, `type`, description)
SELECT 'usercode', '', '系统', '用户码'
WHERE NOT EXISTS (SELECT 1
                  FROM setting
                  WHERE `setting_key` = 'usercode');
