package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-order")
public interface OrdersClient {
    //根据课程id用户id查询订单表中订单的状态
    @GetMapping("/eduorder/order/isBuyCourse/{courseId}/{memberId}")
    public Boolean isBuyCourse(@PathVariable("courseId") String courseId,
                               @PathVariable("memberId") String memberId);
}
