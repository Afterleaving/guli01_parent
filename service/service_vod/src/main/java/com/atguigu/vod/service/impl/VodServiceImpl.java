package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {
    //上传视频到阿里云方法
    @Override
    public String uploadVideoAly(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        //title：上传后显示的名称
        String title = fileName.substring(0,fileName.lastIndexOf("."));
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (Exception e){
            e.printStackTrace();
        }

        UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET,title,fileName,is);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        String videoId = null;
        if (response.isSuccess()){
            videoId = response.getVideoId();
        } else {    //如果设置回调url无效，不影响视频上传，可以返回videoId同时返回错误码。
            videoId = response.getVideoId();
        }
        return videoId;
    }

    //删除多个阿里云视频
    @Override
    public void removeMoreAlyVideo(List<String> videoList) {
        try {
            DefaultAcsClient client = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            //将videoList转换成：1，2，3的形式;   该方法将数组中的元素用‘，’进行隔开一字符串的形式返回
            String videoIds = StringUtils.join(videoList.toArray(), ",");
            request.setVideoIds(videoIds);

            //调用删除视频的方法
            client.getAcsResponse(request);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
