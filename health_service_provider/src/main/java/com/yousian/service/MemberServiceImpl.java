package com.yousian.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yousian.dao.MemberDao;
import com.yousian.pojo.Member;
import com.yousian.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService{
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findmemberbytelephone(String telephone) {
        return memberDao.findmemberbytekephone(telephone);
    }

    @Override
    public void addmember(Member member) {
        String password = member.getPassword();
        if (password!=null){
            password = MD5Utils.md5(password);
            member.setPassword(password)
            ;
        }
        memberDao.addMember(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> list = new ArrayList<>();
        for (String month : months) {
            month = month + "-31";//格式：2019-04-31
            Integer count = memberDao.findMemberCountBeforeDate(month);
            list.add(count);

        }
        return list;
    }
}
