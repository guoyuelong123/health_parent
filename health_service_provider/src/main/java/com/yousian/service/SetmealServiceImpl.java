package com.yousian.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yousian.constant.RedisConstant;
import com.yousian.dao.SetmealDao;
import com.yousian.entity.DeleteChexkitembyidexception;
import com.yousian.entity.PageResult;
import com.yousian.entity.QueryPageBean;
import com.yousian.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService{
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
    @Override
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        setmealDao.add(setmeal);
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        if (checkgroupIds.length>0 && checkgroupIds != null){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("group",checkgroupId);
                map.put("setmeal",setmeal.getId());
                setmealDao.addsetmealandgroup(map);
            }

        }
    }

    @Override
    public PageResult findpage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page=setmealDao.findpage(queryPageBean.getQueryString());

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void deletesetmeal(Integer id) {
        setmealDao.delsetmealandgroup(id);

        try {
            setmealDao.deletesetmeal(id);
        } catch (Exception e) {
            throw new DeleteChexkitembyidexception();

        }
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findsetmealById(Integer id) {
        return setmealDao.findsetmealById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
