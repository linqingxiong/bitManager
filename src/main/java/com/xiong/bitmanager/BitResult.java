package com.xiong.bitmanager;

import lombok.Data;

/**
 * @ClassName BitResult
 * @Description TODO
 * @Author admin
 * @Date 2024/7/16 17:24
 * @Version 1.0
 **/
@Data
public class BitResult<T> {
    private boolean success;
    private T data;
}
