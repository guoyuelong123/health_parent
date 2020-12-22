package com.yousian.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yousian.constant.MessageConstant;
import com.yousian.entity.Result;
import com.yousian.pojo.OrderSetting;
import com.yousian.service.OrderSettingService;
import com.yousian.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("ordersetting")
public class OrderSettingController {
    @Reference
    OrderSettingService orderSettingService;
    //上传文件也就是添加
    @RequestMapping("upload")
    public Result upload(@RequestParam MultipartFile excelFile){

        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            ArrayList<OrderSetting> orderSettingArrayList=new ArrayList<>();
            for (String[] strings : list) {
                orderSettingArrayList.add(new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1])));
            }
            orderSettingService.addorderSetting(orderSettingArrayList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }
    //查一个月的
    @RequestMapping("getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){

        try {
            List<Map> list=orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }
    //编辑预定数量的
    @RequestMapping("editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){//前台传过来的是json所以要加注解

        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }

    }
}
