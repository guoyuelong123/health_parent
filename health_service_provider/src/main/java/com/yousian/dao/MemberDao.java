package com.yousian.dao;

import com.yousian.pojo.Member;

public interface MemberDao {
    Member findmemberbytekephone(String telephone);

    void addMember(Member member);

    Integer findMemberCountBeforeDate(String month);

    Integer findMemberCountByDate(String today);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);
}
