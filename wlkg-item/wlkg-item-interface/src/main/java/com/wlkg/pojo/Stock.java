package com.wlkg.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long sku_id;
    private Integer seckill_stock;
    private Integer seckill_total;
    private Integer stock;

}
