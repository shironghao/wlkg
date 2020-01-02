package com.wlkg.mapper;

import com.wlkg.vo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderMapper extends Mapper<Order> {
    @Select("SELECT o.*,s.* FROM tb_order o JOIN tb_order_status s on o.order_id = s.order_id\n" +
            " WHERE o.user_id = #{userId} and s.status = #{status}")
    List<Order> selectByStatus(@Param("userId") String userId, @Param("status") Integer status);
}
