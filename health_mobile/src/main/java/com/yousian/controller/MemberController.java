package com.yousian.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.yousian.constant.MessageConstant;
import com.yousian.constant.RedisMessageConstant;
import com.yousian.entity.Result;
import com.yousian.pojo.Member;
import com.yousian.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("login")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    MemberService memberService;
    @RequestMapping("check")
    public Result check(@RequestBody Map map, HttpServletResponse response){
        String telephone = (String)map.get("telephone");//手机号
        String validateCode = (String)map.get("validateCode");//验证码
        String s = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);//redis里保存的验证码
        if (validateCode!=null && s!=null && s.equals(validateCode)){//判断
          //判断成功判断是否是会员 如果不是的话要自动添加会员
            Member member=memberService.findmemberbytelephone(telephone);
            if (member==null){//没有这个用户添加
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.addmember(member);
            }
            //登录成功写入cookie跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);//三十天
            response.addCookie(cookie);
            String s1 = JSON.toJSON(member).toString();//把用户信息转成json串保存的redis里 这里的开发原因是语文session在集群中表现不佳所以使用redis模拟一个session
            jedisPool.getResource().setex(telephone,60*30,s1);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else {
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
