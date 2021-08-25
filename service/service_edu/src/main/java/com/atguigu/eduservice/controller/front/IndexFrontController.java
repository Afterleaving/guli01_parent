package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {
    @Resource
    private EduCourseService courseService;
    @Resource
    private EduTeacherService teacherService;

    //查询前8条热门课程，查询前4条名师
    @GetMapping("/index")
    public R index(){
        //查询前8条热门课程
        List<EduCourse> hotCourse = courseService.getHotCourse();

        //查询前4条名师
        List<EduTeacher> hotTeacher = teacherService.getHotTeacher();
        return R.ok().data("eduList",hotCourse).data("teacherList",hotTeacher);
    }
}
