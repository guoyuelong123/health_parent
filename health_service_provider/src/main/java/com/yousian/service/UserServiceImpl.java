package com.yousian.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yousian.dao.PermissionDao;
import com.yousian.dao.RoleDao;
import com.yousian.dao.UserDao;
import com.yousian.pojo.Permission;
import com.yousian.pojo.Role;
import com.yousian.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserSerivce.class)
@Transactional
public class UserServiceImpl implements UserSerivce {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public User finduserbyname(String s) {
        //根据用户名查用户如何它关联的角色以及角色关联的权限
        User finduserbyname = userDao.finduserbyname(s);
        if (finduserbyname==null){
            return null;
        }
        Set<Role> roleSet=roleDao.findrolebyuserid(finduserbyname.getId());
        for (Role role : roleSet) {
            Set<Permission> permissionSet=permissionDao.findpermissionbyroleid(role.getId());
            role.setPermissions(permissionSet);
        }
        finduserbyname.setRoles(roleSet);
        return finduserbyname;
    }
}
