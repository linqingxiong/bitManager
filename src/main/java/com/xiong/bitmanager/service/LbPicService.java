package com.xiong.bitmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        QueryWrapper<LbPic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        return lbPicMapper.selectList(queryWrapper);
    }

    public LbPic savePic(LbPic pic) {
        lbPicMapper.insert(pic);
        return pic;
    }
}
