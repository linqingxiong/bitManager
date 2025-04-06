package com.xiong.bitmanager.common.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
/**
 * @ClassName MyMetaObjectHandler
 * @Description TODO
 * @Author admin
 * @Date 2024/5/29 11:55
 * @Version 1.0
 **/

public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("operation_time", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 这里可以设置更新时间的填充逻辑，如果需要的话
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
