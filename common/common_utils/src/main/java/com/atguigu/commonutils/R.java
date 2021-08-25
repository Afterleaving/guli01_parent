package com.atguigu.commonutils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一结果类
 */
@Data
public class R {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data = new HashMap<>();

    private R(){}   //让其他类不能随便new该类对象（将构造方法私有化）
    //方法都返回R对象，方便链式编程
    //成功静态方法
    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }
    //失败静态方法
    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }

    public R success(boolean success){
        this.success = success;
        return this;
    }

    public R message(String message){
        this.message = message;
        return this;
    }

    public R code(Integer code){
        this.code = code;
        return this;
    }

    public R data(String key,Object value){
        this.getData().put(key,value);
        return this;
    }

    public R data(Map<String,Object> data){
        this.setData(data);
        return this;
    }
}
