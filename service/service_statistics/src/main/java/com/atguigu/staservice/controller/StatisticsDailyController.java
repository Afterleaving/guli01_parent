package com.atguigu.staservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.service.StatisticsDailyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author lwl
 * @since 2021-08-15
 */
@RestController
@RequestMapping("/staservice/sta")
@CrossOrigin
public class StatisticsDailyController {

    @Resource
    private StatisticsDailyService dailyService;

    //统计某一天的注册人数,生成统计数据
    @GetMapping("/registerCount/{day}")
    public R registerCount(@PathVariable("day") String day){
        dailyService.registerCount(day);
        return R.ok();
    }

    //图表显示，返回两部分数据，日期json数组，数量json数组
    @GetMapping("/showData/{type}/{begin}/{end}")
    public R showData(@PathVariable("type")String type,
                      @PathVariable("begin")String begin,
                      @PathVariable("end") String end)
    {
        Map<String,Object> map = dailyService.getShowData(type,begin,end);
        return R.ok().data(map);
    }

}

