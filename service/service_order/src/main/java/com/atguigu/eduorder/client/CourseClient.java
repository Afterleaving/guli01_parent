package com.atguigu.eduorder.client;

import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-edu")
public interface CourseClient {
    //1.通过课程id查询课程信息
    @GetMapping("/eduservice/coursefront/getCourse/{courseId}")
    public CourseWebVoOrder getCourse(@PathVariable("courseId")String courseId);
}
