package com.xiong.bitmanager.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.bitmanager.pojo.dao.LbProductMapper;
import com.xiong.bitmanager.pojo.po.LbProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LbProductService extends ServiceImpl<LbProductMapper, LbProduct> {

    @Autowired
    private LbProductMapper lbProductMapper;

    public List<LbProduct> getAllProducts() {
        return lbProductMapper.selectList(null);
    }

    public LbProduct saveProduct(LbProduct product) {
        lbProductMapper.insert(product);
        return product;
    }
}
