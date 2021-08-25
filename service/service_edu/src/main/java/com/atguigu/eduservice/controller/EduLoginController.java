package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin    //解决跨域问题：ip、端口号、协议（三者任何一个不同都会造成跨域问题）
public class EduLoginController {
    //登录
    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }

    //获取用户信息
    @GetMapping("/info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://j.gifs.com/z6pR27.gif");
    }
}
