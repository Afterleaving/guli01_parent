package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "service-vod",fallback = VodFileDegradeFeignClient.class)
@Component
public interface VodClient {

    //@PathVariable注解一定要指定参数名称，否则出错
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id")String id);

    //删除多个阿里云视频的方法
    //参数为多个视频id: List videoList
    //注意接口调用接口中有@RequestParam：传入的参数一定要和里面的“value”值相同才能成功
    @DeleteMapping("/eduvod/video/deleteBatch")
    public R deleteBatch(@RequestParam("videoIds") List<String> videoList);
}
