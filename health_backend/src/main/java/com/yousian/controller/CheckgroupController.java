package com.yousian.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yousian.constant.MessageConstant;
import com.yousian.entity.PageResult;
import com.yousian.entity.QueryPageBean;
import com.yousian.entity.Result;
import com.yousian.pojo.CheckGroup;
import com.yousian.service.CheckgroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("checkgroup")
public class CheckgroupController {
    @Reference
    CheckgroupService checkgroupService;
    //检查组添加
    @RequestMapping("add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {

        try {
            checkgroupService.add(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }

    }
 //检查组分页查
    @RequestMapping("findPageGroup")
    public PageResult findPageGroup(@RequestBody QueryPageBean queryPageBean) {


        PageResult pageResult = checkgroupService.findPageGroup(queryPageBean);

        return pageResult;
    }
//根据id查检查组
    @RequestMapping("findById")
    public Result findById(Integer id) {

        try {
            CheckGroup checkGroup = checkgroupService.findgroupbyid(id);
            return new Result(true, "查询成功", checkGroup);
        } catch (Exception e) {
            return new Result(false, "查询失败");

        }
    }
//根据groupid查itemid的数组回显
    @RequestMapping("findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {

        try {
            List<Integer> list=checkgroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true,"查询成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }

    }
    //编辑检查组
    @RequestMapping("editgroup")
    public Result editgroup(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {

        try {
            checkgroupService.editgroup(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }

    }
    //添加套餐时的查询检查组全部
    @RequestMapping("findAllgroup")
    public Result findAllgroup(){
        try {
            List<CheckGroup> list=checkgroupService.findAllgroup();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
    //删除
    @RequestMapping("deletegroup")
    public Result deletegroup(int id){
        try {
            checkgroupService.deletegroup(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }
}
