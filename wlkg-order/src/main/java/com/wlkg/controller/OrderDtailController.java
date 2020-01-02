package com.wlkg.controller;

import com.wlkg.auth.pojo.UserInfo;
import com.wlkg.filter.LoginInterceptor;
import com.wlkg.pojo.PageResult;
import com.wlkg.service.OrderDtailService;
import com.wlkg.vo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderDtailController {

    @Autowired
    private OrderDtailService orderDtailService;

    //订单首页分页查询

    @GetMapping("/list")
    public ResponseEntity<PageResult<Order>> queryOrderDetailByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                    @RequestParam(value = "rows", defaultValue = "5") Integer rows
                                                                   ) {


        UserInfo user = LoginInterceptor.getLoginUser();



        PageResult<Order> orders = orderDtailService.selectOrderDetailByPages(page, rows, String.valueOf(user.getId()));


        return ResponseEntity.ok(orders);
    }


    //状态区分查询
    @GetMapping("/list/status")
    public ResponseEntity<PageResult<Order>> queryOrderDetailByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                    @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                                    @RequestParam(value = "status") Integer status) {

        UserInfo user = LoginInterceptor.getLoginUser();

        PageResult<Order> orders = orderDtailService.selectOrderDetailByPage(page, rows, String.valueOf(user.getId()),status);


        return ResponseEntity.ok(orders);
    }
}
