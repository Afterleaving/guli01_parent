package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author lwl
 * @since 2021-08-02
 */
public interface EduTeacherService extends IService<EduTeacher> {

    public void pageQuery(Page<EduTeacher> page, TeacherQuery teacherQuery);

    List<EduTeacher> getHotTeacher();

    Map<String, Object> getTeacherFrontList(Page<EduTeacher> page);
}
