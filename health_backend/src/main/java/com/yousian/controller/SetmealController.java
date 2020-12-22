package com.yousian.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yousian.constant.MessageConstant;
import com.yousian.constant.RedisConstant;
import com.yousian.entity.DeleteChexkitembyidexception;
import com.yousian.entity.PageResult;
import com.yousian.entity.QueryPageBean;
import com.yousian.entity.Result;
import com.yousian.pojo.Setmeal;
import com.yousian.service.SetmealService;
import com.yousian.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    SetmealService setmealService;
    //图片上传
@RequestMapping("upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
    String originalFilename = imgFile.getOriginalFilename();
    String name= UUID.randomUUID()+originalFilename.substring(originalFilename.lastIndexOf(".")-1);
    try {
        QiniuUtils.upload2Qiniu(imgFile.getBytes(),name);
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,name);
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,name);
    } catch (IOException e) {
        e.printStackTrace();
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }
}
@RequestMapping("add")
    public Result add(Integer[] checkgroupIds, @RequestBody Setmeal setmeal){

    try {
        setmealService.add(checkgroupIds,setmeal);
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    } catch (Exception e) {
        e.printStackTrace();
        return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
    }

}
@RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
    PageResult pageResult=setmealService.findpage(queryPageBean);
return pageResult;
}
@RequestMapping("delete")
    public Result delete(Integer id){

    try {
        setmealService.deletesetmeal(id);
        return new Result(true,"删除成功");
    } catch (Exception e) {
        e.printStackTrace();
        if (e instanceof DeleteChexkitembyidexception){
            return new Result(false,"此项有关联不能删除请重新选择");
        }
        return new Result(false,"删除失败");
    }
}

}
