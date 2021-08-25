package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {
    @Resource
    private VodService vodService;

    //上传视频到阿里云方法
    @PostMapping("/uploadAlyVideo")
    public R uploadAlyVideo(MultipartFile file){
        //返回上传视频id
        String videoId = vodService.uploadVideoAly(file);
        return R.ok().data("videoId",videoId);
    }

    //根据视频id删除视频
    @DeleteMapping("/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id")String id){
        try {
            DefaultAcsClient client = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();

            request.setVideoIds(id);
            client.getAcsResponse(request);
            return R.ok();
        } catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }

    //删除多个阿里云视频的方法
    //参数为多个视频id: List videoIds
    @DeleteMapping("/deleteBatch")
    public R deleteBatch(@RequestParam("videoIds")List<String> videoIds){
        vodService.removeMoreAlyVideo(videoIds);
        return R.ok();
    }

    //通过视频id获取视频凭证
    @GetMapping("/getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id){
        try {
            DefaultAcsClient client = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);

            //2.创建request、response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

            //3.向request中设置视频的id值
            request.setVideoId(id);

            //4.通过初始化对象传递数据，获取
            response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();

            return R.ok().data("playAuth",playAuth);
        } catch (Exception e){
            throw new GuliException(20001,"获取凭证失败");
        }
    }


}
