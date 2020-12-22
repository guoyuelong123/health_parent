package com.yousian.dao;

import com.github.pagehelper.Page;
import com.yousian.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckgroupDao {
    void add(CheckGroup checkGroup);

    void addcheckitemidandcheckgroupid(Map<String, Integer> map);

    Page<CheckGroup> findPageGroup(String queryString);

    CheckGroup findgroupbyid(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void editgroup(CheckGroup checkGroup);

    void delCheckItemIdsByCheckGroupId(Integer id);

    void deletegroup(int id);

    Long findcountcheckgroupid(int id);

    void deletegroup_item(int id);

    List<CheckGroup> findAllgroup();
}
