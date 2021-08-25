package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author lwl
 * @since 2021-08-08
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    @Resource
    private EduVideoService videoService;
    @Resource
    private VodClient vodClient;

    //添加小节
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }

    //删除小节，删除对应阿里云视频
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable("videoId")String videoId){
        //根据小节id获取到视频id
        EduVideo eduVideo = videoService.getById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();
        //判断小节里面是否有视频
        if (videoSourceId != null){
            //远程调用vod微服务：根据视频id删除视频
            vodClient.removeAlyVideo(videoSourceId);
        }
        //删除小节
        videoService.removeById(videoId);
        return R.ok();
    }

    //根据id查询小节
    @GetMapping("/getVideo/{videoId}")
    public R getVideo(@PathVariable("videoId")String videoId){
        EduVideo eduVideo = videoService.getById(videoId);
        return R.ok().data("video",eduVideo);
    }

    //修改小节 TODO
    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return R.ok();
    }
}

