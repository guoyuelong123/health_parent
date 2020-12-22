package com.yousian.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yousian.constant.MessageConstant;
import com.yousian.entity.Result;
import com.yousian.service.MemberService;
import com.yousian.service.ReportService;
import com.yousian.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("report")
public class ReportController {
    @Reference
    MemberService memberService;
    @Reference
    SetmealService setmealService;
    @Reference
    ReportService reportService;
//折线表
    @RequestMapping("getMemberReport")
    public Result getMemberReport(){
        //要返回的map
        Map<String,Object> map=new HashMap<>();
        //放月的集合
        List<String> months=new ArrayList<>();

        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        //先往前推12个月
        calendar.add(Calendar.MONTH,-12);
        for(int i = 0;i<12;i++){
            //再通过遍历往后一个月一个月的退
            calendar.add(Calendar.MONTH,1);
            //然后保存到存放月的集合里
            months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        //放到map里
        map.put("months",months);
        //放数子的集合
        List<Integer> memberCount = memberService.findMemberCountByMonth(months);
        map.put("memberCount",memberCount);
        return  new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);


    }
    //饼型表
    @RequestMapping("getSetmealReport")
    public Result getSetmealReport(){
        List<Map<String,Object>> list = setmealService.findSetmealCount();
        Map<String,Object> map = new HashMap();
        map.put("setmealCount",list);
        List<String> setmealNames = new ArrayList<>();
        for(Map<String,Object> m:list){
            String name = (String)m.get("name");
            setmealNames.add(name);
        }
        map.put("setmealNames",setmealNames);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);


    }
    //运营数据统计
    @RequestMapping("getBusinessReportData")
    public Result getBusinessReportData(){

        try {
            Map<String,Object> map=reportService.getBusinessReportData();
            return new Result(true,"查询成功",map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }


    }
    //excel表上穿加下载
    //整体流程大概是1.先从数据库中取出参数
    //2.通过String filePath = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";获取在项目内的excel表
    //3.基于提供的Excel模板文件在内存中创建一个Excel表格对象 XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
    //4.//读取第一个工作表XSSFSheet sheet = excel.getSheetAt(0);用第一个工作表获取第三行第五列因为行是索引0开始的并进行传值XSSFRow row = sheet.getRow(2);row.getCell(5).setCellValue(reportDate);
    //5.如果有集合可以模仿下面的遍历方式进行遍历添加
    //6.通过两个头一个流的方使使用输出流进行表格下载,基于浏览器作为客户端下载并关流
    //            OutputStream out = response.getOutputStream();//这里就是两个头一个流
    //            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
    //            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
    //            excel.write(out);
    //            out.flush();
    //            out.close();
    //            excel.close();
    @RequestMapping("exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String,Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            String filePath = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
            //基于提供的Excel模板文件在内存中创建一个Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //读取第一个工作表
            XSSFSheet sheet = excel.getSheetAt(0);
            //用第一个工作表获取第三行第五列因为行是索引0开始的
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期把数据传到在已经设计好的excel表里
            //下面以此类推
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数
            //热门套餐在excel里是从第十二行开始的
            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");//获取那么
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);//遍历并++下面进行输入
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //使用输出流进行表格下载,基于浏览器作为客户端下载
            OutputStream out = response.getOutputStream();//这里就是两个头一个流
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            excel.write(out);

            out.flush();
            out.close();
            excel.close();
            return null;
        }catch (Exception e){
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }
}
