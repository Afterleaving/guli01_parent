package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author lwl
 * @since 2021-08-15
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Resource
    private UcenterClient ucenterClient;

    @Override
    public void registerCount(String day) {
        //1.远程调用查询出某一天注册人数
        R r = ucenterClient.countRegister(day);
        int countRegister = (int) r.getData().get("countRegister");

        //2.将查出的数据存入统计表中
        //2.1 先判断数据库中是否存在当天的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        StatisticsDaily daily = baseMapper.selectOne(wrapper);

        if (daily == null){
            //不存在，则插入数据
            daily = new StatisticsDaily();
            daily.setDateCalculated(day);
            daily.setRegisterNum(countRegister);
            daily.setVideoViewNum(RandomUtils.nextInt(100,200));
            daily.setLoginNum(RandomUtils.nextInt(100,200));
            daily.setCourseNum(RandomUtils.nextInt(100,200));
            baseMapper.insert(daily);
        } else {
            //存在，则跟新数据
            daily.setRegisterNum(countRegister);
            baseMapper.updateById(daily);
        }


    }

    //返回数据，用作图表显示：前端只接收两个数组（x,y）
    //x轴数据：data_calculatedList     y轴数据：numDataList
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("register_num",type);
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

        List<String> data_calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();

        for (StatisticsDaily daily : staList) {
            data_calculatedList.add(daily.getDateCalculated());
            switch (type) {
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        Map<String,Object> map = new HashMap<>();
        map.put("data_calculatedList",data_calculatedList);
        map.put("numDataList",numDataList);

        return map;
    }
}
