package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class ScheduledTask {
    @Resource
    private StatisticsDailyService dailyService;


    /**
     * 测试
     * 每天七点到二十三点每五秒执行一次
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void task1(){
        System.out.println("***********task1执行了.......");
    }

    //在每天凌晨1点，把前一天的数据进行数据查询添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2(){
        //使用工具类获取前一天的日期
        dailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }

}
