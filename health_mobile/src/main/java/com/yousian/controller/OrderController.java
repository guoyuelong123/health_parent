package com.yousian.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.yousian.constant.MessageConstant;
import com.yousian.constant.RedisMessageConstant;
import com.yousian.entity.Result;
import com.yousian.pojo.Order;
import com.yousian.service.OrderService;
import com.yousian.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    OrderService orderService;
    @RequestMapping("submit")
    public Result submit(@RequestBody Map map){
        System.out.println( map.get("setmealId"));
        //取出电话号
        String telephone = (String)map.get("telephone");
        String s = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //取出在jedis里的验证码区出验证码
        String validateCode = (String)map.get("validateCode");
        //比较验证码
        if (telephone!=null && validateCode!=null && s.equals(validateCode)){
            //成功则添加这里还加了一个orderType形容是怎么预约的
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
           //service处理信息
            Result result= null;
            try {
                result = orderService.orderAdd(map);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //如果验证码发送成功
            if (result.isFlag()){

                try {
                    //给用户发送一个成功短信
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,map.get("orderDate").toString());
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }else {
            //不成功返回失败星系
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }


    }
    @RequestMapping("findById")
    public Result findById(Integer id){

        try {
            Map map=orderService.findorderSuccess(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
