package com.wlkg.api;

import com.wlkg.pojo.PageResult;
import com.wlkg.pojo.Sku;
import com.wlkg.pojo.Spu;
import com.wlkg.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    @GetMapping("/spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);

    @GetMapping("/spu/detail/{id}")
     SpuDetail querySpuDetailById(@PathVariable("id") Long id);

    @GetMapping("/sku/list")
     List<Sku> querySkuBySpuId(@RequestParam("id") Long id);

    @GetMapping("/spu/{id}")
     Spu querySpuById(@PathVariable("id") Long spuId);

    @GetMapping("/sku")
    Sku querySkuById(@RequestParam("skuId") Long skuId);
}
