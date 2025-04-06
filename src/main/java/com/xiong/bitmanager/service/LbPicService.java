package com.xiong.bitmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.bitmanager.pojo.dao.LbPicMapper;
import com.xiong.bitmanager.pojo.po.LbPic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LbPicService extends ServiceImpl<LbPicMapper, LbPic> {

    @Autowired
    private LbPicMapper lbPicMapper;

    public List<LbPic> getPicsByProductId(Long productId) {
        LambdaQueryWrapper<LbPic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LbPic::getProductId, productId).orderByAsc(LbPic::getId);
        return lbPicMapper.selectList(queryWrapper);
    }

    public LbPic savePic(LbPic pic) {
        lbPicMapper.insert(pic);
        return pic;
    }

    public IPage<LbPic> getPicsPage(int page, int size) {
        QueryWrapper<LbPic> queryWrapper = new QueryWrapper<>();
        return page(new Page<>(page, size), queryWrapper);
    }
}
