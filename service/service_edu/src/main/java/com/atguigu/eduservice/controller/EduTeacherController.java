package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author lwl
 * @since 2021-08-02
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {
    @Resource
    private EduTeacherService teacherService;

    //查询讲师表所有数据
    @ApiOperation(value = "查询所有讲师列表")
    @GetMapping("/findAll")
    public R findAllTeacher(){
        List<EduTeacher> teacherList = teacherService.list(null);
        return R.ok().data("items",teacherList);
    }

    //通过id删除讲师
    @ApiOperation(value = "通过id删除讲师")
    @DeleteMapping("/{id}")
    public R removeTeacher(@PathVariable("id")String id){
        boolean flag = teacherService.removeById(id);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //分页查询讲师
    @ApiOperation(value = "分页查询讲师")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable("current") long current,
                      @PathVariable("limit") long limit)
    {
        //try { int i = 10/0; } catch (Exception e){ throw new GuliException(20001,"执行了自定义异常处理"); }

        Page<EduTeacher> teacherPage = new Page<>(current,limit);
        teacherService.page(teacherPage,null);

        List<EduTeacher> records = teacherPage.getRecords();
        long total = teacherPage.getTotal();
        return R.ok().data("rows",records).data("total",total);
    }

    //复杂条件分页查询讲师
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable("current")long current,
                                  @PathVariable("limit")long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery)
    {
        Page<EduTeacher> page = new Page<>(current,limit);
        teacherService.pageQuery(page,teacherQuery);
        List<EduTeacher> records = page.getRecords();
        long total = page.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }

    //添加讲师
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher teacher){
        boolean res = teacherService.save(teacher);
        if (res){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //修改讲师信息
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher teacher){
        boolean res = teacherService.updateById(teacher);
        if (res){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //通过id查询讲师
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable("id")String id){
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

}

