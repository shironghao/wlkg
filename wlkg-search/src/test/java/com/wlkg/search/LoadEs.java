package com.wlkg.search;

import com.wlkg.WlkgSearchService;
import com.wlkg.client.GoodsClient;
import com.wlkg.pojo.Goods;
import com.wlkg.pojo.PageResult;
import com.wlkg.pojo.Spu;
import com.wlkg.repository.GoodsRepository;
import com.wlkg.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞鸟
 * @create 2019-12-16 16:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LoadEs {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository repository;


    @Test
    public void loadData() {
        //将数据库中的数据导入索引库中
        int size = 0;
        int page = 1;
        do {
            //1.查询数据
            PageResult<Spu> pages = goodsClient.querySpuByPage(page, 50, true, null);
            size = pages.getItems().size();
            //3.导入索引
            List<Spu> list = pages.getItems();
            List<Goods> goodsList = new ArrayList<>();
            for (Spu spu : list) {
                goodsList.add(goodsService.buildGoods(spu));
            }
            repository.saveAll(goodsList);
            page ++;
        } while (size == 50 );
    }
}

