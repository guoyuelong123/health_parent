package com.yousian.service;

import com.yousian.entity.DeleteChexkitembyidexception;
import com.yousian.entity.PageResult;
import com.yousian.entity.QueryPageBean;
import com.yousian.pojo.CheckItem;

import java.util.ArrayList;

public interface CheckitemService {
    void addCheckitem(CheckItem checkItem);

    PageResult findall(QueryPageBean queryPageBean);

    void delete(int id) throws DeleteChexkitembyidexception;
    //利用id查检查项
    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    ArrayList<CheckItem> findallcheckitem(int id);

    ArrayList<CheckItem> findallcheckitemadd();
}
