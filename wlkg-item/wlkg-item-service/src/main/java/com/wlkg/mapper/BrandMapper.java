package com.wlkg.mapper;

import com.wlkg.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    void insertCategoryBrand(@Param("cid") long cid,@Param("bid") Long id);

   /* @Select("SELECT category_id from tb_category_brand where brand_id=#{bid}")
    long selectCategory(@Param("bid") long id);*/

   @Delete("delete from tb_category_brand where brand_id = #{id}")
    void deleteCategoryAndBrand(@Param("id") long id);


}
