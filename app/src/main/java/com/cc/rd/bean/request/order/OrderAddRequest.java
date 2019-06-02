package com.cc.rd.bean.request.order;

import lombok.Data;

/**
 * @program: OrderAddRequest
 * @description: 发起活动订单
 * @author: cchen
 * @create: 2019-03-29 13:23
 */
@Data
public class OrderAddRequest {
    /**
     * 活动主题
     */
    private String orderTitle;

    /**
     * 活动内容
     */
    private String orderContent;

    /**
     * 开始时间
     */
    private Long orderStarts;

    /**
     * 结束时间
     */
    private Long orderEnds;

    /**
     * 是否公开
     */
    private Integer orderType;

    /**
     * 店家
     */
    private Long shopId;

    /**
     * 人数上限
     */
    private Integer orderNum;

    /**
     * 城市
     */
    private Integer orderAdcode;

    /**
     * 区
     */
    private Integer orderCityCode;

    /**
     * 经度
     */
    private Double orderLongitude;

    /**
     * 纬度
     */
    private Double orderLatitude;

    private String orderAddress;

}

