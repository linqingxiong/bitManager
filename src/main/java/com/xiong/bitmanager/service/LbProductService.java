package com.xiong.bitmanager.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.bitmanager.pojo.dao.LbPicMapper;
import com.xiong.bitmanager.pojo.dao.LbProductMapper;
import com.xiong.bitmanager.pojo.po.LbPic;
import com.xiong.bitmanager.pojo.po.LbProduct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LbProductService extends ServiceImpl<LbProductMapper, LbProduct> {

    private final LbProductMapper lbProductMapper;
    private final LbPicMapper lbPicMapper;

    public LbProductService(LbProductMapper lbProductMapper, LbPicMapper lbPicMapper) {
        this.lbProductMapper = lbProductMapper;
        this.lbPicMapper = lbPicMapper;
    }

    public List<LbProduct> getAllProducts() {
        return list();
    }

    @Transactional
    public LbProduct saveProduct(LbProduct product) {
        lbProductMapper.insert(product);
        List<String> pics = product.getPics();
        for (String pic : pics) {
            LbPic lbPic = new LbPic();
            lbPic.setImgUrl(pic);
            lbPic.setProductId(product.getId());
            lbPicMapper.insert(lbPic);
        }
        return product;
    }
    public IPage<LbProduct> getProductsPage(int page, int size,String name) {
        LambdaQueryWrapper<LbProduct> queryWrapper = new LambdaQueryWrapper<>();
        return page(new Page<>(page, size), queryWrapper.like(StrUtil.isNotBlank(name),LbProduct::getTitle1, name));
    }

    @Transactional
    public void updateProduct(LbProduct product) {
        lbProductMapper.updateById(product);
        List<String> pics = product.getPics();
        lbPicMapper.delete(new LambdaQueryWrapper<LbPic>().eq(LbPic::getProductId, product.getId()));
        for (String pic : pics) {
            LbPic lbPic = new LbPic();
            lbPic.setImgUrl(pic);
            lbPic.setProductId(product.getId());
            lbPicMapper.insert(lbPic);
        }
    }
}
