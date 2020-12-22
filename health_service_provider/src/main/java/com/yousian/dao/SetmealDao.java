package com.yousian.dao;

import com.github.pagehelper.Page;
import com.yousian.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void add(Setmeal setmeal);

    void addsetmealandgroup(Map<String, Integer> map);

    Page<Setmeal> findpage(String queryString);

    void delsetmealandgroup(Integer id);

    void deletesetmeal(Integer id);

    List<Setmeal> getSetmeal();

    Setmeal findsetmealById(Integer id);

    List<Map<String, Object>> findSetmealCount();
}
