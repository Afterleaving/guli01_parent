package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.educenter.entity.RegisterVo;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author lwl
 * @since 2021-08-11
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {
    @Resource
    private UcenterMemberService memberService;

    //用户登录
    @PostMapping("/login")
    public R login(@RequestBody UcenterMember member){
        //返回token值，使用jwt生成
        String token = memberService.login(member);
        return R.ok().data("token",token);
    }


    //用户注册
    @PostMapping("/register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //根据token获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据id获取用户信息
        UcenterMember ucenterMember = memberService.getById(memberId);
        return R.ok().data("userInfo",ucenterMember);
    }

    //根据id查询用户信息
    @GetMapping("/getMember/{memberId}")
    public UcenterMemberOrder getMember(@PathVariable String memberId){
        UcenterMember member = memberService.getById(memberId);

        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }

    //查询某一天的注册人数
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable("day") String day){
        Integer count = memberService.countRegisterDay(day);
        return R.ok().data("countRegister",count);
    }

}

