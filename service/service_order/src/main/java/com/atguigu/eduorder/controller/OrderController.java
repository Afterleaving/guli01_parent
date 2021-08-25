package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author lwl
 * @since 2021-08-13
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class OrderController {
    @Resource
    private OrderService orderService;

    //生成订单
    @PostMapping("/createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //创建订单，返回订单号
        String orderNo = orderService.createOrders(courseId,memberId);
        return R.ok().data("orderId",orderNo);
    }

    //根据订单号查询订单信息
    @GetMapping("/getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("item",order);
    }

    //根据课程id用户id查询订单表中订单的状态
    @GetMapping("/isBuyCourse/{courseId}/{memberId}")
    public Boolean isBuyCourse(@PathVariable("courseId") String courseId,
                         @PathVariable("memberId") String memberId)
    {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.select("status");
        Order order = orderService.getOne(wrapper);

        Integer status = order.getStatus();
        if (status == 1){
            return true;
        } else {
            return false;
        }
    }

}

