package com.yousian.dao;

import com.yousian.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findorderbyorder(Order order);

    void addOrder(Order order);


    Map findDetailsById(Integer id);

    Integer findOrderCountByDate(String today);

    Integer findOrderCountAfterDate(String thisWeekMonday);

    Integer findVisitsCountByDate(String today);

    Integer findVisitsCountAfterDate(String thisWeekMonday);

    List<Map> findHotSetmeal();
}
