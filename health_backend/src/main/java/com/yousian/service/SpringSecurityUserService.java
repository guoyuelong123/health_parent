package com.yousian.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yousian.pojo.Permission;
import com.yousian.pojo.Role;
import com.yousian.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserSerivce userSerivce;
    //根据用户名查询数据库获取用户信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userSerivce.finduserbyname(username);
        if (user==null){
            //用户名不存在
            return null;
        }
        //把权限名保存在list里
        List<GrantedAuthority> list=new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
          list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //遍历权限集合为用户授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        org.springframework.security.core.userdetails.User securityuser=new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
        return securityuser;
    }
}
