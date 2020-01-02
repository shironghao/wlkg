package com.wlkg.mapper;

import com.wlkg.pojo.Brand;
import com.wlkg.pojo.Spu;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuMapper extends Mapper<Spu> {
    @Select("select b.* from tb_brand b INNER JOIN tb_category_brand tc ON tc.brand_id=b.id where tc.category_id=#{id}")
    List<Brand> selectBrandId(long id);
}
