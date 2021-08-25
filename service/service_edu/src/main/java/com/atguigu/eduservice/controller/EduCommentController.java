package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author lwl
 * @since 2021-08-13
 */
@RestController
@RequestMapping("/eduservice/comment")
@CrossOrigin
public class EduCommentController {
    @Resource
    private EduCommentService commentService;
    @Resource
    private UcenterClient ucenterClient;

    //分页查询评论
    @GetMapping("/getCommentList/{page}/{limit}")
    public R getCommentList(@PathVariable long page,
                            @PathVariable long limit,
                            String courseId)
    {
        Page<EduComment> pageParam = new Page<>(page, limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        commentService.page(pageParam,wrapper);
        List<EduComment> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }

    //添加评论功能
    @PostMapping("/addComment")
    public R addComment(@RequestBody EduComment comment, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("请先登录");
        }

        comment.setMemberId(memberId);
        UcenterMemberOrder ucenterInfo  = ucenterClient.getMember(memberId);
        comment.setAvatar(ucenterInfo.getAvatar());
        comment.setNickname(ucenterInfo.getNickname());

        boolean res = commentService.save(comment);
        return R.ok().data("success",res);
    }
}

