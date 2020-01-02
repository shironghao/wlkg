package com.wlkg.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "tb_order")
public class Order {
    private Long orderId;
    private Long totalPay;
    private Long actualPay;
    private String promotionIds;
    private Boolean paymentType;
    private Long postFee;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    private String shippingName;
    private String shippingCode;
    private String userId;
    private String buyerMessage;
    private String buyerNick;
    private Boolean buyerRate;
    private String receiverState;
    private String receiverCity;
    private String receiverDistrict;
    private String receiverAddress;
    private String receiverMobile;
    private String receiverZip;
    private String receiver;
    private Integer invoiceType;
    private Integer sourceType;
    @Transient
    private List<Order_detail> orderDetail;
    @Transient
    private Order_Status orderStatus;





}
