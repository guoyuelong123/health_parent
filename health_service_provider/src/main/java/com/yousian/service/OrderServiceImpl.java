package com.yousian.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yousian.constant.MessageConstant;
import com.yousian.dao.MemberDao;
import com.yousian.dao.OrderDao;
import com.yousian.dao.OrderSettingDao;
import com.yousian.entity.Result;
import com.yousian.pojo.Member;
import com.yousian.pojo.Order;
import com.yousian.pojo.OrderSetting;
import com.yousian.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Override
    public Result orderAdd(Map orderInfo) throws Exception{
        //查看设置的时间内是否可以预约
        String orderDate = (String) orderInfo.get("orderDate");//预约日期
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting=orderSettingDao.findorderbydate(date);
        if (orderSetting==null){
            //这个时间内不能预约因为没有设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //查看在用户预约的时间内是否已经满了
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if(reservations>=number){
            //预约已经满了不能设置
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //查看用户是否重复预约
        String telephone = (String) orderInfo.get("telephone");
        Member member=memberDao.findmemberbytekephone(telephone);
       if (member!=null){
           //用户id
           Integer id = member.getId();
           //申请日期
           Date date2 = DateUtils.parseString2Date(orderDate);
           //套餐id

           String setmealId = (String) orderInfo.get("setmealId");
           List<Order> orderList=orderDao.findorderbyorder(new Order(id,date2,Integer.parseInt(setmealId)));
           if (orderList!=null && orderList.size()>0){
               return new Result(false,MessageConstant.HAS_ORDERED);
           }
       }else {
           //由于上面没有找到所以不是会员要指定添加会员
           member = new Member();
           member.setName((String)orderInfo.get("name"));
           member.setSex((String)orderInfo.get("sex"));
           member.setPhoneNumber(telephone);
           member.setRegTime(new Date());
           member.setIdCard((String)orderInfo.get("idCard"));
           memberDao.addMember(member);

       }
       //预约成功更新当日已预约人数
        Date date2 = DateUtils.parseString2Date(orderDate);
        Order order = new Order(member.getId(),date2,Order.ORDERTYPE_WEIXIN,Order.ORDERSTATUS_NO,Integer.parseInt((String)orderInfo.get("setmealId")));
        orderDao.addOrder(order);
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editNumberByDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());


    }

    @Override
    public Map findorderSuccess(Integer id) throws Exception {
        Map map = orderDao.findDetailsById(id);

        if(map!=null){
            Date orderDate = (Date)map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;


    }
}
