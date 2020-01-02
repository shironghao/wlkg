package com.wlkg.mapper;

import com.wlkg.vo.Order_detail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderDtailMapper extends Mapper<Order_detail> {


    @Select("SELECT tb_order_detail.* FROM tb_order  JOIN tb_order_detail ON tb_order.order_id=tb_order_detail.order_id WHERE tb_order.user_id=#{userId} ")
    List<Order_detail> selectOrderDetailByPage(@Param("userId") String userId);

}
