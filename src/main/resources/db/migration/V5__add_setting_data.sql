INSERT INTO setting (`setting_key`, `setting_value`, `type`, description)
SELECT 'license', '', '系统', '凭证'
    WHERE NOT EXISTS (SELECT 1
                  FROM setting
                  WHERE `setting_key` = 'license');
