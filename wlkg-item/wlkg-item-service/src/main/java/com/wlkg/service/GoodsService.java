package com.wlkg.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlkg.mapper.*;
import com.wlkg.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;


    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper mapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<Spu> querySpuByPageAndSort(Integer page, Integer rows, Boolean saleable, String key) {

        PageHelper.startPage(page, Math.min(rows, 100));
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (saleable != null) {
            criteria.orEqualTo("saleable", saleable);
        }
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");

        }
        criteria.andEqualTo("valid",true);
        example.setOrderByClause("last_update_time desc");
        List<Spu> spus = spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);
        for (Spu spu : spus) {
            List<String> names = categoryService.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

            spu.setCname(StringUtils.join(names, "/"));
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spu.setBname(brand.getName());

        }

        return new PageResult<>(pageInfo.getTotal(), spus);
    }

    //添加商品
    public void add(Spu spu) {
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spuMapper.insert(spu);
        spu.getSpuDetail().setSpuId(spu.getId());
        spuDetailMapper.insert(spu.getSpuDetail());
        List<Sku> skus = spu.getSkus();
        List<Stock> stocks = new ArrayList<>();
        if (skus != null && skus.size() != 0) {
            for (Sku sku : skus) {

                sku.setSpuId(spu.getId());
                // 默认不参与任何促销
                sku.setCreateTime(new Date());
                sku.setLastUpdateTime(sku.getCreateTime());
                skuMapper.insert(sku);
                Stock stock = new Stock();
                stock.setSku_id(sku.getId());
                stock.setStock(sku.getStock().intValue());
                stocks.add(stock);


            }
            stockMapper.insertList(stocks);
            sendMessage(spu.getId(),"insert");

        }
    }

    public List<Brand> selectBrandId(long id) {

        return spuMapper.selectBrandId(id);
    }

    //修改第一步回显SpuDetail对象
    public SpuDetail selectSpuDetail(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);

    }

    //修改第二步回显sku对象
    public List<Sku> selectSku(Long id) {
        Sku sku1 = new Sku();
        sku1.setSpuId(id);
        List<Sku> skus=new ArrayList<>();
        List<Sku> skus1 = skuMapper.select(sku1);
        for(Sku sku: skus1){
            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());

            sku.setStock(stock.getStock());

            skus.add(sku);
        }
        return skus;


    }

    //修改业务信息
    @Transactional
    public void update(Spu spu) {
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        //查询sku
        List<Sku> skuList = skuMapper.select(sku);

        if (!CollectionUtils.isEmpty(skuList)) {


            //删除stock
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);

            //删除sku和stock
            skuMapper.delete(sku);

        }

        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);

        int count = spuMapper.updateByPrimaryKeySelective(spu);

        spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        //修改detail


        //新增sku和stock
        List<Sku> skus = spu.getSkus();
        List<Stock> stocks = new ArrayList<>();
        for (Sku sku1 : skus) {
            if (!sku1.getEnable()) {
                continue;
            }
            // 保存sku
            sku1.setSpuId(spu.getId());
            // 默认不参与任何促销
            sku1.setCreateTime(new Date());
            sku1.setLastUpdateTime(sku1.getCreateTime());
            this.skuMapper.insert(sku1);

            // 保存库存信息
            Stock stock = new Stock();
            stock.setSku_id(sku1.getId());


            stock.setStock(sku1.getStock().intValue());
            stocks.add(stock);
        }

        //批量新增库存
        stockMapper.insertList(stocks);
        sendMessage(spu.getId(),"update");
    }

    //删除商品第一步查询商品
    //第二步修改商品valid的属性为false
    public void deleteSpu(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        spu.setValid(false);
        spuMapper.updateByPrimaryKey(spu);
    }

    public void updateSpu(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        spu.setSaleable(!spu.getSaleable());
        spuMapper.updateByPrimaryKey(spu);
    }

    public Spu querySpuById(Long spuId) {

        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

//        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
//
//        spu.setBname(brand.getName());
//
//        Category category = mapper.selectByPrimaryKey(spu.getCid3());
//        spu.setCname(category.getName());


        //查询sku

        spu.setSkus(selectSku(spuId));
        //查询detail
        spu.setSpuDetail(selectSpuDetail(spuId));
        return spu;

    }

    private void sendMessage(Long id, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Sku selectBySku(Long skuId) {

        Sku sku = skuMapper.selectByPrimaryKey(skuId);
        return sku;
    }
}
