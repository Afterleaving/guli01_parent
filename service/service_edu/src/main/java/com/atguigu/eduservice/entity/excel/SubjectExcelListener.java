package com.atguigu.eduservice.entity.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    //因为SubjectExcelListener不能交给spring进行管理，需要自己new，不能注入其他对象
    private EduSubjectService subjectService;

    public SubjectExcelListener(){};

    public SubjectExcelListener(EduSubjectService subjectService){
        this.subjectService = subjectService;
    }

    //读取excel内容
    @Override
    public void invoke(SubjectData data, AnalysisContext analysisContext) {
        if (data == null){
            throw new GuliException(20001,"文件数据为空");
        }
        EduSubject oneSubject = this.exitOneSubject(subjectService,data.getOneSubjectName());
        if (oneSubject == null){
            oneSubject = new EduSubject();
            oneSubject.setTitle(data.getOneSubjectName());
            oneSubject.setParentId("0");
            subjectService.save(oneSubject);
        }

        String pid = oneSubject.getId();
        EduSubject twoSubject = this.exitTwoSubject(subjectService, data.getTwoSubjectName(),pid);
        if (twoSubject == null){
            twoSubject = new EduSubject();
            twoSubject.setParentId(pid);
            twoSubject.setTitle(data.getTwoSubjectName());
            subjectService.save(twoSubject);
        }
    }

    //判断一级分类不能重复添加
    private EduSubject exitOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        return subjectService.getOne(wrapper);
    }

    //判断二级分类不能重复添加
    public EduSubject exitTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.ne("parent_id",pid);
        return subjectService.getOne(wrapper);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
