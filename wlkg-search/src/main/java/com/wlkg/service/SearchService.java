package com.wlkg.service;

import com.wlkg.client.BrandClient;
import com.wlkg.client.CategoryClient;
import com.wlkg.client.GoodsClient;
import com.wlkg.pojo.*;
import com.wlkg.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;

    public PageResult<Goods> search(SearchRequest request) {


        String key = request.getKey();
        // 判断是否有搜索条件，如果没有，直接返回null。不允许搜索全部商品
        if (StringUtils.isBlank(key)) {
            return null;
        }

        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 1、对key进行全文检索查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", key).operator(Operator.AND));

        // 2、通过sourceFilter设置返回的结果字段,我们只需要id、skus、subTitle
        queryBuilder.withSourceFilter(new FetchSourceFilter(
                new String[]{"id", "skus", "subTitle"}, null));

        // 3、分页
        // 准备分页参数
        int page = request.getPage();
        int size = request.getSize();
        queryBuilder.withPageable(PageRequest.of(page - 1, size));
        //排序
        String sortBy = request.getSortBy();
        Boolean desc = request.getDescending();
        if (StringUtils.isNotBlank(sortBy)) {
            //如果不为空，则进行排序
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc ? SortOrder.DESC : SortOrder.ASC));
        }

        // 4、查询，获取结果
        Page<Goods> pageInfo = this.goodsRepository.search(queryBuilder.build());

        // 封装结果并返回
        List<Goods> goods = pageInfo.getContent();
        long total = pageInfo.getTotalElements();
        long totalPage = pageInfo.getTotalPages();
        return new PageResult<>(total, totalPage, goods);

    }

    public void createIndex(Long id) {

        //查询spu
        Spu spu = goodsClient.querySpuById(id);

        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> names = categories.stream().map(c -> c.getName()).collect(Collectors.toList());

        spu.setCname(StringUtils.join(names, "/"));
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        spu.setBname(brand.getName());


        //构建goods
        Goods goods =goodsService.buildGoods(spu);
        //存入索引库
        goodsRepository.save(goods);

    }

    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);

    }
}