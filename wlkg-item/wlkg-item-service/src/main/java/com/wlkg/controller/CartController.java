package com.wlkg.controller;

import com.wlkg.pojo.Sku;
import com.wlkg.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private  CartService cartService;



    //查询购物车的价格
    @GetMapping("/sku/list/ids")
    public ResponseEntity<List<Sku>> getCart(@RequestParam("ids") List<Long> ids){

        List<Sku> skus= cartService.selectSku(ids);

        return ResponseEntity.ok(skus);

    }
}
