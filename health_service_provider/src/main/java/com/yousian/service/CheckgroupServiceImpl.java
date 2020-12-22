package com.yousian.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yousian.dao.CheckgroupDao;
import com.yousian.entity.PageResult;
import com.yousian.entity.QueryPageBean;
import com.yousian.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckgroupService.class)
@Transactional
public class CheckgroupServiceImpl implements CheckgroupService{
@Autowired
private CheckgroupDao checkgroupDao;
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {

        checkgroupDao.add(checkGroup);
        System.out.println(checkGroup.getId());
        if(checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkGroupid", checkGroup.getId());
                map.put("checkitemId", checkitemId);
                checkgroupDao.addcheckitemidandcheckgroupid(map);
            }
        }
    }

    @Override
    public PageResult findPageGroup(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page=checkgroupDao.findPageGroup(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findgroupbyid(Integer id) {
        return checkgroupDao.findgroupbyid(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkgroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void editgroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        //编辑
        checkgroupDao.editgroup(checkGroup);
        //删除原来的关联
        checkgroupDao.delCheckItemIdsByCheckGroupId(checkGroup.getId());
        //再添加新的关联
        for (Integer checkitemId : checkitemIds) {

                Map<String, Integer> map = new HashMap<>();
                map.put("checkGroupid", checkGroup.getId());
                map.put("checkitemId", checkitemId);
                checkgroupDao.addcheckitemidandcheckgroupid(map);

        }
    }

    @Override
    public void deletegroup(int id){
            checkgroupDao.deletegroup_item(id);
            checkgroupDao.deletegroup(id);


    }

    @Override
    public List<CheckGroup> findAllgroup() {
        return checkgroupDao.findAllgroup();
    }
}
