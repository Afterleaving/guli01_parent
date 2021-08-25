package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.excel.SubjectExcelListener;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author lwl
 * @since 2021-08-05
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            InputStream is = file.getInputStream();
            EasyExcel.read(is, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //课程分类的列表功能(树形)
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //1.查询出所有的一级分类
        QueryWrapper<EduSubject> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapper1);

        //2.查询出所有的二级分类
        QueryWrapper<EduSubject> wrapper2 = new QueryWrapper<>();
        wrapper2.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapper2);

        List<OneSubject> list = new ArrayList<>();

        //3.封装一级分类
        for (EduSubject eduSubject : oneSubjectList) {
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(oneSubject.getTitle());
            //简化写法
            BeanUtils.copyProperties(eduSubject,oneSubject);

            //4.封装二级分类
            List<TwoSubject> twoSubjects = new ArrayList<>();
            for (EduSubject subject : twoSubjectList) {
                if (oneSubject.getId().equals(subject.getParentId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(subject,twoSubject);
                    twoSubjects.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoSubjects);
            list.add(oneSubject);
        }
        return list;
    }
}
