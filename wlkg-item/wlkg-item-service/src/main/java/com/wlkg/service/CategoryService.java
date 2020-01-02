package com.wlkg.service;

import com.wlkg.mapper.BrandMapper;
import com.wlkg.mapper.CategoryMapper;
import com.wlkg.pojo.Brand;
import com.wlkg.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper mapper;

    public List<Category> getCategory(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> list = mapper.select(category);
        return list;
    }

    //添加
    public boolean addCategory(Category category) {
        return mapper.insertSelective(category) > 0;
    }

    public boolean edit(Category category) {
        return mapper.updateByPrimaryKeySelective(category) > 0;
    }

    //删除
    public boolean delete(Long id) {
        Category category = mapper.selectByPrimaryKey(id);
        if (category.getIsParent()) {

            deleteIsparent(category);

        }


        MysetIsParent(id);

        return true;
    }

    //是父目录，准备递归删除

    public void deleteIsparent(Category category) {

        Category category1 = new Category();
        category1.setParentId(category.getId());
        List<Category> categories = mapper.select(category1);
        for (Category category2 : categories) {
            if (!category2.getIsParent()) {
                mapper.deleteByPrimaryKey(category.getId());
                continue;
            }
            deleteIsparent(category2);
        }
    }

    //判断传进来的id对象不是父目录时
    public void MysetIsParent(Long id) {
        Category category = new Category();
        category.setParentId(id);
        List<Category> categories = mapper.select(category);
        if (categories.size() == 0) {
            Category category1 = new Category();
            category1.setId(id);
            category1.setIsParent(false);
            mapper.updateByPrimaryKeySelective(category1);
        }
    }

    public List<Category> queryByBrandId(Long bid) {

        return mapper.queryByBrandId(bid);
    }

    public List<String> queryNameByIds(List<Long> asList) {
      return   mapper.selectByIdList(asList).stream().map(Category::getName).collect(Collectors.toList());
    }


    public List<Category> queryByIds(List<Long> ids) {

        List<Category> list = this.mapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)) {
            //throw new WlkgException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;

    }
}
