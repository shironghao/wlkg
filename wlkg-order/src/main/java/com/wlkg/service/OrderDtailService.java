package com.wlkg.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlkg.mapper.OrderDtailMapper;
import com.wlkg.mapper.OrderMapper;
import com.wlkg.mapper.OrderStatusMapper;
import com.wlkg.pojo.PageResult;
import com.wlkg.vo.Order;
import com.wlkg.vo.Order_Status;
import com.wlkg.vo.Order_detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDtailService {

    @Autowired
    private OrderMapper orderMapper;


    @Autowired
    private OrderDtailMapper orderDtailMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;



    //状态区分查询

    public PageResult<Order> selectOrderDetailByPage(Integer page, Integer rows, String userId, Integer status) {
        PageHelper.startPage(page, rows);





            List<Order> orders = orderMapper.selectByStatus(userId, status);
            for(Order order:orders ){

                Order_detail orderDetail = new Order_detail();
                orderDetail.setOrderId(order.getOrderId());
                order.setOrderDetail(orderDtailMapper.select(orderDetail));

            }




            PageInfo<Order> pageInfo = new PageInfo<>(orders);
            return new PageResult<>(pageInfo.getTotal(), orders);





    }


    //订单首页分页查询

    public PageResult<Order> selectOrderDetailByPages(Integer page, Integer rows, String user_id) {

        PageHelper.startPage(page, rows);
        Order order1 = new Order();
        order1.setUserId(user_id);

        List<Order> orders = orderMapper.select(order1);

        for (Order order : orders) {
            Order_detail orderDetail = new Order_detail();
            orderDetail.setOrderId(order.getOrderId());

            order.setOrderDetail(orderDtailMapper.select(orderDetail));

            Order_Status orderStatus = orderStatusMapper.selectByPrimaryKey(order.getOrderId());
            order.setOrderStatus(orderStatus);
        }
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        return new PageResult<>(pageInfo.getTotal(), orders);

    }
}
