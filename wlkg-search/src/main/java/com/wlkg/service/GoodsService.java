package com.wlkg.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wlkg.client.CategoryClient;
import com.wlkg.client.GoodsClient;
import com.wlkg.client.SpecificationClient;
import com.wlkg.common.utils.JsonUtils;
import com.wlkg.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;

    public Goods buildGoods(Spu spu){
        String all=spu.getTitle();
        all +=" "+spu.getCname().replace('/',' ');
        all +=" "+spu.getBname();

        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
        System.out.println(skus);
        List<Long> prices=new ArrayList<>();
        List<Map<String,Object>> skuList=new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String,Object> skuMap=new HashMap<>();
            skuMap.put("id",sku.getId());
            skuMap.put("title",sku.getTitle());
            skuMap.put("price",sku.getPrice());
            skuMap.put("image", StringUtils.substringBefore(sku.getImages(),","));
            skuList.add(skuMap);


        });
        List<SpecParam> params=specificationClient.querySpecParam(null,spu.getCid3(),true,null);
        SpuDetail spuDetail=goodsClient.querySpuDetailById(spu.getId());
        Map<Long,String> genericSpec= JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        //定义spec对应的map
        HashMap<String, Object> specs = new HashMap<>();
        //对规格进行遍历，并封装spec，其中spec的key是规格参数的名称，值是商品详情中的值
        for (SpecParam param : params) {
            //key是规格参数的名称
            String key = param.getName();
            Object value = "";

            if (param.getGeneric()) {
                //参数是通用属性，通过规格参数的ID从商品详情存储的规格参数中查出值
                value = genericSpec.get(param.getId());
                if (param.getNumeric()) {
                    //参数是数值类型，处理成段，方便后期对数值类型进行范围过滤
                    value = chooseSegment(value.toString(), param);
                }
            } else {
                //参数不是通用类型
                value = specialSpec.get(param.getId());
            }
            value = (value == null ? "其他" : value);
            //存入map
            specs.put(key, value);
        }


        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());

        goods.setAll(all);//拼接：标题+分类+品牌
        goods.setSkus(JsonUtils.serialize(skuList));//设置skus的json结构
        goods.setPrice(prices);//设置skus的价格
        goods.setSpecs(specs);//设置规格
        goods.setCreateTime(spu.getCreateTime());
        goods.setBrandId(spu.getBrandId());
        goods.setSubTitle(spu.getSubTitle());

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

}
