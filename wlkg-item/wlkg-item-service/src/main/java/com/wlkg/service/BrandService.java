package com.wlkg.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlkg.mapper.BrandMapper;
import com.wlkg.mapper.CategoryMapper;
import com.wlkg.pojo.Brand;
import com.wlkg.pojo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;



    public PageResult<Brand> getBrand(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        PageHelper.startPage(page,rows);//开启分页
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("name", "%" + key + "%").orEqualTo("letter",key);
        }
        if (StringUtils.isNotBlank(sortBy)){
            //排序
            String  order = sortBy+(desc ? " DESC" : " ASC");
            example.setOrderByClause(order);
        }
        List<Brand> brands = brandMapper.selectByExample(example);
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        return new PageResult<>(pageInfo.getTotal(),brands);
    }

    public void insert(Brand brand,List<Long> cids) {

        brandMapper.insert(brand);
        for(long cid:cids){
            brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    public void delete(long id) {

        brandMapper.deleteCategoryAndBrand(id);
        brandMapper.deleteByPrimaryKey(id);

    }

    public void update(Brand brand,List<Long> cids) {

        brandMapper.updateByPrimaryKeySelective(brand);
        brandMapper.deleteByPrimaryKey(brand.getId());
        for(Long cid:cids){

            brandMapper.insertCategoryBrand(cid, brand.getId());

        }
    }




        public Brand queryById(Long id) {
            return brandMapper.selectByPrimaryKey(id);
        }

}
