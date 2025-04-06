package com.xiong.bitmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.bitmanager.pojo.dao.PutOnlineTaskMapper;
import com.xiong.bitmanager.pojo.po.LbProduct;
import com.xiong.bitmanager.pojo.po.PutOnlineTask;
import org.springframework.stereotype.Service;

/**
 * @ClassName PutOnlineTaskService
 * @Description TODO
 * @Author admin
 * @Date 2025/3/13 15:38
 * @Version 1.0
 **/
@Service
public class PutOnlineTaskService extends ServiceImpl<PutOnlineTaskMapper, PutOnlineTask> {

    public IPage<PutOnlineTask> getPutOnlineTasksPage(int page, int size, Long productId) {
        LambdaQueryWrapper<PutOnlineTask> queryWrapper = new LambdaQueryWrapper<>();
        return page(new Page<>(page, size),
                queryWrapper.eq(productId != null && productId != 0, PutOnlineTask::getProductId, productId)
                        .orderByDesc(PutOnlineTask::getCreateTime));
    }

    public void statByProductId(LbProduct product) {

        // 统计成功数量
        QueryWrapper<PutOnlineTask> successWrapper = new QueryWrapper<>();
        successWrapper.eq("product_id", product.getId())
                .eq("status", "完成");
        Long successCount = this.baseMapper.selectCount(successWrapper);
        product.setPutOnlineTaskSuccessCount(successCount.intValue());
        // 统计总数量（不限制status）
        QueryWrapper<PutOnlineTask> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("product_id", product.getId());
        Long totalCount = this.baseMapper.selectCount(totalWrapper);
        product.setPutOnlineTaskTotalCount(totalCount.intValue());
    }
}
