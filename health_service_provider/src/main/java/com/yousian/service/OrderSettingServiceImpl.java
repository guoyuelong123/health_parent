package com.yousian.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yousian.dao.OrderSettingDao;
import com.yousian.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService{
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void addorderSetting(ArrayList<OrderSetting> orderSettingArrayList) {
        if (orderSettingArrayList.size()>0 && orderSettingArrayList!=null){
            for (OrderSetting orderSetting : orderSettingArrayList) {
                //判断日期是否存在
                long num=orderSettingDao.finddatebyorderdate(orderSetting.getOrderDate());
                if (num>0){
                    //日期存在就修改
                    orderSettingDao.updateorderbydate(orderSetting);
                }else {
                    //日期不存在就添加
                    orderSettingDao.addorder(orderSetting);
                }
            }

        }
    }
//查询当月的的日期
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String tou=date+"-1";
        String wei=date+"-31";
        Map<String,String> sdate=new HashMap<>();
        sdate.put("tou",tou);
        sdate.put("wei",wei);
        List<OrderSetting> list=orderSettingDao.getOrderSettingByMonth(sdate);
        List<Map> list1=new ArrayList<>();
        if (list.size()>0 && list!=null){
            for (OrderSetting orderSetting : list) {
                Map<String,Object> map=new HashMap<>();
                map.put("date",orderSetting.getOrderDate().getDate());//获取日期数字（几号）
                map.put("number",orderSetting.getNumber());
                map.put("reservations",orderSetting.getReservations());
                list1.add(map);
            }
        }
        return list1;
    }
//单个设置预约人数
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //查看这个日期是否设置过
        long finddatebyorderdate = orderSettingDao.finddatebyorderdate(orderSetting.getOrderDate());
       if (finddatebyorderdate>0){//设置过修改即可
           orderSettingDao.updateorderbydate(orderSetting);
       }else {//没有设置添加
           orderSettingDao.addorder(orderSetting);
       }
    }
}
