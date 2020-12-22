package com.yousian.service;

import com.yousian.pojo.Member;

import java.util.List;

public interface MemberService {

    Member findmemberbytelephone(String telephone);

    void addmember(Member member);

    List<Integer> findMemberCountByMonth(List<String> months);
}
