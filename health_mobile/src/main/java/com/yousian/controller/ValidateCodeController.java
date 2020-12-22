package com.yousian.controller;

import com.aliyuncs.exceptions.ClientException;
import com.yousian.constant.RedisMessageConstant;
import com.yousian.entity.Result;
import com.yousian.utils.SMSUtils;
import com.yousian.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("validataCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("fayezhima")
    public Result fayezhima(String telephone){
        System.out.println(telephone);
        Integer integer = ValidateCodeUtils.generateValidateCode(4);

        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,integer.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,"验证码发送失败");
        }
        //将验证码保存的redis并设置五分钟
      jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,integer.toString());
       return new Result(true,"验证码发送成功");
    }
    @RequestMapping("send4Login")
    public Result send4Login(String telephone){
        System.out.println(telephone);
        Integer integer = ValidateCodeUtils.generateValidateCode(6);

        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,integer.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,"验证码发送失败");
        }
        //将验证码保存的redis并设置五分钟
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,300,integer.toString());
        return new Result(true,"验证码发送成功");
    }
}
