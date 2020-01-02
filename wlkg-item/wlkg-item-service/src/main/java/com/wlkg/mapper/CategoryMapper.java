package com.wlkg.mapper;

import com.wlkg.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>,IdListMapper<Category,Long>{
    @Select("SELECT c.* from tb_category c join tb_category_brand cb on c.id = cb.category_id where cb.brand_id=#{bid}")
    List<Category> queryByBrandId(@Param("bid") Long bid);

}
