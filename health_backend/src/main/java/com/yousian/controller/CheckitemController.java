package com.yousian.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yousian.constant.MessageConstant;
import com.yousian.entity.DeleteChexkitembyidexception;
import com.yousian.entity.PageResult;
import com.yousian.entity.QueryPageBean;
import com.yousian.entity.Result;
import com.yousian.pojo.CheckItem;
import com.yousian.service.CheckitemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("checkitem")
public class CheckitemController {
    @Reference
    CheckitemService checkitemService;
    @RequestMapping("add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkitemService.addCheckitem(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }
    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       PageResult pageResult=checkitemService.findall(queryPageBean);
        List rows = pageResult.getRows();
        System.out.println(rows.toString());

        return pageResult;
    }
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验
    @RequestMapping("delete")
    public Result delete(int id){

        try {
            checkitemService.delete(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DeleteChexkitembyidexception){
                return new Result(false,"有关联组和此项关联，请选择其他项");
            }
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }
    //利用id查检查项
    @RequestMapping("findById")
    public Result findById(Integer id){

        try {
            CheckItem checkItem=checkitemService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }
    //编辑检查项
    @RequestMapping("edit")
    public Result edit(@RequestBody CheckItem checkItem){

        try {
            checkitemService.edit(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }

    }
    @RequestMapping("findAll")
    public Result findAll(Integer id){

        try {
            ArrayList<CheckItem> arrayList=null;
            if (id != null){
                arrayList=checkitemService.findallcheckitem(id);
            }else {
                arrayList=checkitemService.findallcheckitemadd();
            }
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,arrayList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }
    /*@RequestMapping("findAlladd")
    public Result findalladd(){
        try {
            ArrayList<CheckItem> arrayList=checkitemService.findallcheckitemadd();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,arrayList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }*/


}
