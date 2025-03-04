package com.xiong.bitmanager.controller;

import com.xiong.bitmanager.pojo.po.LbProduct;
import com.xiong.bitmanager.service.LbProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class LbProductController {

    @Autowired
    private LbProductService lbProductService;

    @GetMapping
    public List<LbProduct> getAllProducts() {
        return lbProductService.getAllProducts();
    }

    @PostMapping
    public LbProduct saveProduct(@RequestBody LbProduct product) {
        return lbProductService.saveProduct(product);
    }
}
