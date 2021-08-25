package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author lwl
 * @since 2021-08-10
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
public class BannerAdminController {
    @Resource
    private CrmBannerService bannerService;

    //分页查询banner
    @GetMapping("/pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable("page")long page,
                        @PathVariable("limit")long limit)
    {
        Page<CrmBanner> pageBanner = new Page<>(page,limit);
        bannerService.page(pageBanner,null);
        List<CrmBanner> records = pageBanner.getRecords();
        long total = pageBanner.getTotal();
        return R.ok().data("items",records).data("total",total);
    }

    //添加banner
    @PostMapping("/addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        bannerService.save(crmBanner);
        return R.ok();
    }

    //修改banner
    @PostMapping("/updateBanner")
    public R updateBanner(@RequestBody CrmBanner crmBanner){
        bannerService.updateById(crmBanner);
        return R.ok();
    }

    //通过id查询banner
    @GetMapping("/getBanner/{id}")
    public R getBanner(@PathVariable("id")String id){
        CrmBanner crmBanner = bannerService.getById(id);
        return R.ok().data("banner",crmBanner);
    }

    //删除banner
    @DeleteMapping("/deleteBanner/{id}")
    public R deleteBanner(@PathVariable("id")String id){
        bannerService.removeById(id);
        return R.ok();
    }

}

