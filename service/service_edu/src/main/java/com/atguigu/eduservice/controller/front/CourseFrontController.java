package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.eduservice.client.OrdersClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseQueryVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {
    @Resource
    private EduCourseService courseService;
    @Resource
    private EduChapterService chapterService;
    @Resource
    private OrdersClient ordersClient;

    //条件查询带分页查询课程
    @PostMapping("/getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page,
                                @PathVariable long limit,
                                @RequestBody(required = false) CourseQueryVo courseQueryVo)
    {
        Page<EduCourse> coursePage = new Page<>(page,limit);
        Map<String,Object> map = courseService.getCourseFrontList(coursePage,courseQueryVo);
        return R.ok().data(map);
    }

    //课程详情功能
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId, HttpServletRequest request){
        //1.通过课程id查询：课程介绍、课程讲师、课程大纲、课程分类
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);

        //2.根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

        //3.判断是否已经购买该课程(远程调用)
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (memberId == null){  //判断是否登录
            return R.error().code(28004).message("请先登录");
        }
        boolean isBuyCourse = ordersClient.isBuyCourse(courseId, memberId);

        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",isBuyCourse);
    }

    //通过id查询课程信息
    @GetMapping("/getCourse/{courseId}")
    public CourseWebVoOrder getCourse(@PathVariable String courseId){
        CourseWebVo baseCourseInfo = courseService.getBaseCourseInfo(courseId);

        CourseWebVoOrder eduCourseOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(baseCourseInfo,eduCourseOrder);
        return eduCourseOrder;
    }

}
