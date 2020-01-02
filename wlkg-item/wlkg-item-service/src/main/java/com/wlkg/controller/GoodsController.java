package com.wlkg.controller;

import com.wlkg.pojo.*;
import com.wlkg.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                            @RequestParam(value = "saleable", required = false) Boolean saleable,
                                                            @RequestParam(value = "key", required = false) String key
    ){


       PageResult<Spu> list= goodsService.querySpuByPageAndSort(page,rows,saleable,key);
       return ResponseEntity.ok(list);
    }

//添加
    @PostMapping("/goods")
    public ResponseEntity<Boolean> add( @RequestBody Spu spu){
        goodsService.add(spu);
        return ResponseEntity.ok(true);

    }
    @GetMapping("/brand/cid/{id}")
    public ResponseEntity<List<Brand>> selectBrand(@PathVariable("id")long id){
        List<Brand> list=goodsService.selectBrandId(id);



        return ResponseEntity.ok(list);
    }



    //修改
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id){
        SpuDetail spuDetail=goodsService.selectSpuDetail(id);
        return ResponseEntity.ok(spuDetail);
    }

    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id){
        List<Sku> list=goodsService.selectSku(id);
        return ResponseEntity.ok(list);
    }



    //真正修改
    @PutMapping("/goods")
    public ResponseEntity<Boolean> update( @RequestBody Spu spu){

        goodsService.update(spu);
        return ResponseEntity.ok(true);

    }


    //删除商品
    @DeleteMapping("/goods/delete")
    public ResponseEntity<Boolean> deleteSpu(@RequestParam("id")Long id){
        goodsService.deleteSpu(id);
        return ResponseEntity.ok(true);
    }


    //下架或者下架商品
    @PutMapping("/goods/saleable")
    public ResponseEntity<Boolean> updateSpu(@RequestParam("id")Long id){

        goodsService.updateSpu(id);
        return ResponseEntity.ok(true);
    }


    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */


    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long spuId){
        Spu spu = this.goodsService.querySpuById(spuId);
        return ResponseEntity.ok(spu);
    }

    @GetMapping(value = "/sku")
    public ResponseEntity<Sku> querySkuById(@RequestParam("skuId") Long skuId){
        Sku sku = goodsService.selectBySku(skuId);
        return ResponseEntity.ok(sku);
    }

}
