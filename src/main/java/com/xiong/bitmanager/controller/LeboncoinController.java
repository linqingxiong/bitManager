package com.xiong.bitmanager.controller;

import com.xiong.bitmanager.pojo.po.LbProduct;
import com.xiong.bitmanager.service.LeboncoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leboncoin")
public class LeboncoinController {

    @Autowired
    private LeboncoinService leboncoinService;

    @GetMapping("/execute-context-loads")
    public String executeContextLoads(LbProduct lbProduct) {
        try {
            leboncoinService.executeContextLoads(lbProduct);
            return "Execution completed successfully!";
        } catch (Exception e) {
            return "Execution failed: " + e.getMessage();
        }
    }
}
