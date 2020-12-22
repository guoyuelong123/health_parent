package com.yousian.dao;

import com.yousian.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findpermissionbyroleid(Integer id);
}
