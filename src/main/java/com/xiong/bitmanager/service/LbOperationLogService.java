package com.xiong.bitmanager.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.bitmanager.pojo.dao.LbOperationLogMapper;
import com.xiong.bitmanager.pojo.po.LbOperationLog;
import com.xiong.bitmanager.pojo.po.LbProduct;
import org.springframework.stereotype.Service;

@Service
public class LbOperationLogService extends ServiceImpl<LbOperationLogMapper, LbOperationLog> {


    public IPage<LbOperationLog> getOperationLogsPage(int page, int size, Integer productId) {
        LambdaQueryWrapper<LbOperationLog> queryWrapper = new LambdaQueryWrapper<>();
        return page(new Page<>(page, size), queryWrapper.eq(ObjectUtil.isNotEmpty(productId),LbOperationLog::getProductId, productId).orderByDesc(LbOperationLog::getOperationTime));
    }

    public void statByProductId(LbProduct product) {
        int totalCount = Math.toIntExact(count(new LambdaQueryWrapper<LbOperationLog>()
                .eq(LbOperationLog::getProductId, product.getId())));
        int successCount = Math.toIntExact(count(new LambdaQueryWrapper<LbOperationLog>()
                .eq(LbOperationLog::getProductId, product.getId()).eq(LbOperationLog::getOperationResult, "成功")));
        product.setBrowserTotalCount(totalCount);
        product.setBrowserSuccessCount(successCount);
    }
}
