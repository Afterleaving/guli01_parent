package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.RegisterVo;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.HttpRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author lwl
 * @since 2021-08-11
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Resource
    private RedisTemplate redisTemplate;

    //登录的方法
    @Override
    public String login(UcenterMember member) {
        String mobile = member.getMobile();
        String password = member.getPassword();

        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"手机号或密码为空");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if (ucenterMember == null){
            throw new GuliException(20001,"手机号不存在");
        }

        //判断密码是否正确
        //因为存储到数据库中的密码经过了加密
        //此时就需将输入的密码进行加密在和数据库比较
        //加密方式：MD5

        if (!MD5.encrypt(password).equals(ucenterMember.getPassword())){
            throw new GuliException(20001,"密码错误");
        }

        //判断该用户是否被禁用
        if (ucenterMember.getIsDisabled()){
            throw new GuliException(20001,"已经被禁用");
        }

        //登录成功
        String token = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());

        return token;
    }

    //注册方法
    @Override
    public void register(RegisterVo registerVo) {
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        //如果数据为空，则注册失败
        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)){
            throw new GuliException(20001,"注册失败");
        }

        //判断验证码是否正确
        String cacheCode = (String) redisTemplate.opsForValue().get(mobile);
        if (!code.equals(cacheCode)){
            throw new GuliException(20001,"验证码错误");
        }

        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        int count = baseMapper.selectCount(wrapper);
        if (count > 0){
            throw new GuliException(20001,"该手机号已经注册");
        }

        //添加到数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));  //将密码加密存储进数据库
        member.setNickname(nickname);
        member.setIsDisabled(false);    //设置用户不禁用
        member.setAvatar("https://edu-first11.oss-cn-beijing.aliyuncs.com/z6pR27.gif"); //设置默认头像
        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer countRegisterDay(String day) {
        Integer count = baseMapper.countRegisterDay(day);
        return count;
    }

}
