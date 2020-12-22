package com.yousian.dao;

import com.yousian.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    //查询日期是否重复
    long finddatebyorderdate(Date orderDate);
    //根据日期修改
    void updateorderbydate(OrderSetting orderSetting);
    //添加日期和人数
    void addorder(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(Map<String, String> sdate);

    void editNumberByDate(OrderSetting orderSetting);
//查询在这个时间段内是否有设置预约
    OrderSetting findorderbydate(Date date);
}
