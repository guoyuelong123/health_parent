package com.yousian.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yousian.dao.CheckitemDao;
import com.yousian.entity.DeleteChexkitembyidexception;
import com.yousian.entity.PageResult;
import com.yousian.entity.QueryPageBean;
import com.yousian.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckitemService.class)
@Transactional
public class CheckitemServiceImpl implements CheckitemService {
    @Autowired
    CheckitemDao checkitemDao;
    @Override
    public void addCheckitem(CheckItem checkItem) {
      checkitemDao.addCheckitem(checkItem);
    }

    @Override
    public PageResult findall(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
         Page<CheckItem> page =checkitemDao.findall(queryPageBean.getQueryString());
        List<CheckItem> result = page.getResult();

        return new PageResult(page.getTotal(),result);
    }

    @Override
    public void delete(int id) throws DeleteChexkitembyidexception {
    // 判断检查项是否关联的检查组
        Long count = checkitemDao.findcountcheckitemid(id);
        if (count>0){
         //说明关联到检查组不允许删除 抛出一个异常
             throw new DeleteChexkitembyidexception();
        }else {checkitemDao.delete(id);}

    }
    //利用id查检查项
    @Override
    public CheckItem findById(Integer id) {
        return checkitemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkitemDao.edit(checkItem);
    }

    @Override
    public ArrayList<CheckItem> findallcheckitem(int id) {
        Map<String,Integer> map=new HashMap<>();
        map.put("id",id);
        return checkitemDao.findallcheckitem(map);
    }

    @Override
    public ArrayList<CheckItem> findallcheckitemadd() {
        return checkitemDao.findallcheckitemadd();
    }
}
