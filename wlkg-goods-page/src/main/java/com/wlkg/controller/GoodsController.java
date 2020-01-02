package com.wlkg.controller;

import com.wlkg.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @GetMapping("/item/{id}.html")
    public String toItemPage(Model model, @PathVariable("id") Long id) {
        Map<String, Object> attributes  = goodsService.loadModel(id);
        model.addAllAttributes(attributes);

        // 判断是否需要生成新的页面
        if(!this.goodsService.exists(id)){
            this.goodsService.syncCreateHtml(id);
        }

//        - spu信息
//- spu的详情
//- spu下的所有sku
//- 品牌
//- 商品三级分类
//- 商品规格参数、规格参数组
        return "item";
    }



}
