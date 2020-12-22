package com.yousian.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yousian.constant.MessageConstant;
import com.yousian.entity.Result;
import com.yousian.pojo.Setmeal;
import com.yousian.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @RequestMapping("getSetmeal")
    public Result getSetmeal(){

        try {
            List<Setmeal> setmealList=setmealService.getSetmeal();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }
    @RequestMapping("findsetmealById")
    public Result findsetmealById(Integer id){
        System.out.println(id);
        try {
            Setmeal setmeal=setmealService.findsetmealById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
