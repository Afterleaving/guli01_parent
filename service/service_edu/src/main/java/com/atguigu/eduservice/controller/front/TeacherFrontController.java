package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/eduservice/teacherfront")
public class TeacherFrontController {
    @Resource
    private EduTeacherService teacherService;
    @Resource
    private EduCourseService courseService;

    //分页查询讲师
    @GetMapping("/getTeacherFrontList/{current}/{limit}")
    public R getTeacherFrontList(@PathVariable("current")long current,
                                 @PathVariable("limit")long limit)
    {
        Page<EduTeacher> page = new Page<>(current,limit);
        Map<String,Object> map = teacherService.getTeacherFrontList(page);
        //返回分页所有的数据
        return R.ok().data(map);
    }


    //2.讲师详情功能
    @GetMapping("/getTeacherInfo/{teacherId}")
    public R getTeacherInfo(@PathVariable("teacherId")String teacherId){
        //1.根据讲师id查询讲师的信息
        EduTeacher eduTeacher = teacherService.getById(teacherId);

        //2.根据讲师id查询讲师所讲的课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);
        return R.ok().data("teacher",eduTeacher).data("courseList",courseList);
    }

}
