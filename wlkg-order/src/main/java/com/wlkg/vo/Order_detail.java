package com.wlkg.vo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_order_detail")
public class Order_detail {
    @Id
    private Long id;
    private Long orderId;
    private Long skuId;
    private Integer num;
    private String title;
    private String ownSpec;
    private Long price;
    private String image;


}
