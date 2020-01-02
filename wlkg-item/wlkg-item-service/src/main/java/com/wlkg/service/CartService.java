package com.wlkg.service;

import com.wlkg.mapper.SkuMapper;
import com.wlkg.pojo.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private SkuMapper skuMapper;

    //查询购物车价格
    public List<Sku> selectSku(List<Long> ids) {

        List<Sku> skus=new ArrayList<>();
        for(Long id:ids){
            Sku sku= skuMapper.selectByPrimaryKey(id);
            skus.add(sku);
        }


        return skus;
    }
}
