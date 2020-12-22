package com.yousian.service;

import com.yousian.entity.PageResult;
import com.yousian.entity.QueryPageBean;
import com.yousian.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Integer[] checkgroupIds, Setmeal setmeal);

    PageResult findpage(QueryPageBean queryPageBean);

    void deletesetmeal(Integer id);

    List<Setmeal> getSetmeal();

    Setmeal findsetmealById(Integer id);

    List<Map<String, Object>> findSetmealCount();
}
