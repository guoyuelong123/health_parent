package com.yousian.dao;

import com.yousian.pojo.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findrolebyuserid(Integer id);
}
