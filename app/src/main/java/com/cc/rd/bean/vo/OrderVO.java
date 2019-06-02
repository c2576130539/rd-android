package com.cc.rd.bean.vo;

import com.cc.rd.bean.OrderItem;

import java.util.List;

import lombok.Data;

@Data
public class OrderVO {

    List<OrderItem> orderList;
}
