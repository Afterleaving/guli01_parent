package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.eduorder.client.CourseClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author lwl
 * @since 2021-08-13
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    private UcenterClient ucenterClient;
    @Resource
    private CourseClient courseClient;

    //生成订单
    @Override
    public String createOrders(String courseId, String memberId) {
        //1.通过课程id查询课程信息
        CourseWebVoOrder course = courseClient.getCourse(courseId);

        //2.通过用户id查询用户信息
        UcenterMemberOrder member = ucenterClient.getMember(memberId);

        //3.将数据封装到订单对象中
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setMemberId(memberId);
        order.setNickname(member.getNickname());
        order.setMobile(member.getMobile());
        order.setCourseCover(course.getCover());
        order.setCourseId(courseId);
        order.setCourseTitle(course.getTitle());
        order.setTeacherName(course.getTeacherName());
        order.setStatus(0);     //支付状态  0:未支付   1:已支付
        order.setPayType(1);    //支付类型  1:微信    2:支付宝
        order.setTotalFee(course.getPrice());

        //4.将订单存入数据库
        baseMapper.insert(order);

        return order.getOrderNo();
    }
}
