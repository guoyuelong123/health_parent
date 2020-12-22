package com.yousian.service;

import com.yousian.entity.PageResult;
import com.yousian.entity.QueryPageBean;
import com.yousian.pojo.CheckGroup;

import java.util.List;


public interface CheckgroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPageGroup(QueryPageBean queryPageBean);

    CheckGroup findgroupbyid(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void editgroup(CheckGroup checkGroup, Integer[] checkitemIds);

    void deletegroup(int id);

    List<CheckGroup> findAllgroup();
}
