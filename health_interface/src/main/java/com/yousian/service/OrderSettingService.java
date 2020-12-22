package com.yousian.service;

import com.yousian.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface OrderSettingService {

    void addorderSetting(ArrayList<OrderSetting> orderSettingArrayList);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);


}
