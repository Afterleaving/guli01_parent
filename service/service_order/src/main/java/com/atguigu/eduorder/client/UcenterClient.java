package com.atguigu.eduorder.client;

import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-ucenter")
public interface UcenterClient {
    //根据id查询用户信息
    @GetMapping("/educenter/member/getMember/{memberId}")
    public UcenterMemberOrder getMember(@PathVariable String memberId);
}
