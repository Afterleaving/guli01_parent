package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author lwl
 * @since 2021-08-08
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {
    @Resource
    private EduCourseService courseService;


    //查询所有课程列表
    @GetMapping("/getCourseList")
    public R getCourseList(){
        List<EduCourse> courseList = courseService.list(null);
        return R.ok().data("list",courseList);
    }

    //复杂的条件查询课程列表分页
    @PostMapping("/pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(@PathVariable("current")long current,
                                 @PathVariable("limit")long limit,
                                 @RequestBody CourseQuery courseQuery)
    {
        Page<EduCourse> coursePage = new Page<>(current,limit);
        courseService.pageQuery(coursePage,courseQuery);
        List<EduCourse> records = coursePage.getRecords();
        long total = coursePage.getTotal();
        return R.ok().data("rows",records).data("total",total);
    }


    //添加课程信息
    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String cid = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",cid);
    }

    //根据课程id查询课程信息
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable("courseId")String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfo",courseInfoVo);
    }

    //修改课程信息
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("/getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable("id")String id){
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return R.ok().data("publishCourseInfo",coursePublishVo);
    }

    //课程最终发布
    //修改课程状态
    @PostMapping("/publishCourse/{id}")
    public R publishCourse(@PathVariable("id")String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");      //设置课程发布状态
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable("courseId")String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }



}

