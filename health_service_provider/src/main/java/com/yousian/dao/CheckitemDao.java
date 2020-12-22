package com.yousian.dao;


import com.github.pagehelper.Page;
import com.yousian.pojo.CheckItem;

import java.util.ArrayList;
import java.util.Map;

public interface CheckitemDao {
    void addCheckitem(CheckItem checkItem);


    Page<CheckItem> findall(String queryString);

    void delete(int id);
    //查检查项id在关联表中的数量
    Long findcountcheckitemid(int id);
    //利用id查检查项
    CheckItem findById(Integer id);
    //编辑检查项
    void edit(CheckItem checkItem);

    ArrayList<CheckItem> findallcheckitem(Map map);

    ArrayList<CheckItem> findallcheckitemadd();
}
