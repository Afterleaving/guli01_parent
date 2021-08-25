package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author lwl
 * @since 2021-08-05
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {
    @Resource
    private EduSubjectService subjectService;

    //添加课程分类
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file){
        subjectService.saveSubject(file,subjectService);
        return R.ok();
    }

    //课程分类的列表功能(树形)
    @GetMapping("/getAllSubject")
    public R getAllSubject(){
        List<OneSubject> oneSubjects = subjectService.getAllOneTwoSubject();
        return R.ok().data("list",oneSubjects);
    }

}

