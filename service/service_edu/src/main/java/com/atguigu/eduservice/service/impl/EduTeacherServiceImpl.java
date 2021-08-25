package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author lwl
 * @since 2021-08-02
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    //复杂的条件分页查询
    @Override
    public void pageQuery(Page<EduTeacher> page, TeacherQuery teacherQuery){
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if (teacherQuery == null){
            baseMapper.selectPage(page,wrapper);
            return;
        }
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }
        wrapper.orderByDesc("gmt_create",begin);
        baseMapper.selectPage(page,wrapper);
    }

    //查询前4条名师
    @Override
    public List<EduTeacher> getHotTeacher() {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 4");
        List<EduTeacher> eduTeachers = baseMapper.selectList(wrapper);
        return eduTeachers;
    }

    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> page) {
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        baseMapper.selectPage(page,wrapper);

        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        long current = page.getCurrent();
        long pages = page.getPages();
        long size = page.getSize();
        boolean next = page.hasNext();
        boolean previous = page.hasPrevious();

        map.put("total",total);           // 总记录数
        map.put("items",records);   //每页的数据集合
        map.put("current",current);   //当前页
        map.put("size",size);   //每页记录数
        map.put("pages",pages);   //总页数
        map.put("next",next);   //下一页
        map.put("previous",previous);      //上一页

        return map;
    }
}
