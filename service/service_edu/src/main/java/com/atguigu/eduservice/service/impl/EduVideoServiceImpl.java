package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author lwl
 * @since 2021-08-08
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Resource
    private VodClient vodClient;

    //根据课程id删除小节
    @Override
    public void removeByCourseId(String courseId) {
        //根据课程id查询所有视频id
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.select("video_source_id");  //只查询视频id字段
        List<EduVideo> videoList = baseMapper.selectList(wrapper);

        List<String> videoIds = new ArrayList<>();
        //将List<EduVideo> ---》 List<String>
        for (EduVideo eduVideo : videoList) {
            String videoSourceId = eduVideo.getVideoSourceId();
            if (videoSourceId != null){     //判断小节中是否有视频
                videoIds.add(videoSourceId);
            }
        }

        //远程调用实现删除多个阿里云视频
        if (videoIds.size() > 0){   //判断list集合中是否有值
            vodClient.deleteBatch(videoIds);
        }

        QueryWrapper<EduVideo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id",courseId);
        baseMapper.delete(wrapper1);
    }
}
