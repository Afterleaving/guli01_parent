package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.frontvo.CourseQueryVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author lwl
 * @since 2021-08-08
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Resource
    private EduCourseDescriptionService courseDescriptionService;
    @Resource
    private EduChapterService chapterService;
    @Resource
    private EduVideoService videoService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1.向课程表中添加课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert == 0){
            throw new GuliException(20001,"添加课程信息失败");
        }

        //添加之后获取课程id
        String cid = eduCourse.getId();

        //2.向课程简介表中添加简介信息
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述id就是课程id，建立一对一的关系
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);

        return cid;
    }

    //根据id查询课程信息，数据回显
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1.根据课程id查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        //2.根据课程id查询描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);

        //封装数据
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1.修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int res = baseMapper.updateById(eduCourse);
        if (res == 0){
            throw new GuliException(20001,"修改课程信息失败");
        }

        //2.修改描述表
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescription.setId(courseInfoVo.getId());
        courseDescriptionService.updateById(courseDescription);
    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    //复杂分页查询课程列表
    @Override
    public void pageQuery(Page<EduCourse> coursePage, CourseQuery courseQuery) {
        if (courseQuery == null){
            baseMapper.selectPage(coursePage,null);
            return;
        }
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        String begin = courseQuery.getBegin();
        String end = courseQuery.getEnd();

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }
        wrapper.orderByDesc("gmt_create",begin);

        baseMapper.selectPage(coursePage,wrapper);
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //1.根据课程id删除小节
        videoService.removeByCourseId(courseId);

        //2.根据课程id删除章节
        chapterService.removeChapterByCourseId(courseId);

        //3.根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        //4.根据课程id删除课程
        int res = baseMapper.deleteById(courseId);
        if (res == 0){
            throw new GuliException(20001,"删除课程失败");
        }
    }

    //查询前8条热门课程
    @Override
    public List<EduCourse> getHotCourse() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("view_count");
        wrapper.last("limit 8");
        List<EduCourse> eduCourses = baseMapper.selectList(wrapper);
        return eduCourses;
    }

    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseQueryVo courseQueryVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if (courseQueryVo == null) {
            baseMapper.selectPage(coursePage,null);
        }
        if (!StringUtils.isEmpty(courseQueryVo.getSubjectParentId())){//一级分类
            wrapper.eq("subject_parent_id",courseQueryVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseQueryVo.getSubjectId())){//二级分类
            wrapper.eq("subject_id",courseQueryVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseQueryVo.getBuyCountSort())){//销量排序
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseQueryVo.getGmtCreateSort())){//时间排序
            wrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(courseQueryVo.getPriceSort())){//价格排序
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(coursePage, wrapper);
        Map<String,Object> map = new HashMap<>();

        List<EduCourse> courses = coursePage.getRecords();
        long total = coursePage.getTotal();
        long current = coursePage.getCurrent();
        long size = coursePage.getSize();
        long pages = coursePage.getPages();
        boolean hasNext = coursePage.hasNext();
        boolean hasPrevious = coursePage.hasPrevious();

        //把分页数据放入map集合中
        map.put("items",courses);
        map.put("current",current);
        map.put("pages",pages);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);

        return map;
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        CourseWebVo courseInfo = baseMapper.getBaseCourseInfo(courseId);
        return courseInfo;
    }
}
