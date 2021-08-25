package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.service.PayLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author lwl
 * @since 2021-08-13
 */
@RestController
@RequestMapping("/eduorder/paylog")
@CrossOrigin
public class PayLogController {
    @Resource
    private PayLogService payLogService;

    //生成微信支付二维码
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        //返回二维码的地址，还有其他信息
        Map map = payLogService.createNative(orderNo);
        System.out.println("********返回二维码的集合："+map);
        return R.ok().data(map);
    }

    //查询订单支付的状态
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("********查询订单状态map集合："+map);
        if (map == null){
            return R.error().message("支付出错了");
        }
        //如果返回的map不为null，从里面获取订单的状态
        if (map.get("trade_state").equals("SUCCESS")) {
            //向支付表添加记录，更新订单表订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }
}

