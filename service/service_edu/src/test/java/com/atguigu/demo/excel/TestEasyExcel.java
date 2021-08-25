package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    public static void main(String[] args) {
        //实现excel写操作
        //1.设置写入文件夹地址和文件名称
//        String filename = "C:\\Users\\ASUS\\Desktop\\谷粒项目文件\\write.xlsx";

        //2.调用easyExcel里面的方法实现写操作
//        EasyExcel.write(filename,DataDemo.class).sheet("学生列表").doWrite(getData());


        //实现读操作
        String filename = "C:\\Users\\ASUS\\Desktop\\谷粒项目文件\\write.xlsx";

        EasyExcel.read(filename,DataDemo.class,new ExcelListener()).sheet().doRead();
    }

    private static List<DataDemo> getData(){
        List<DataDemo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DataDemo demo = new DataDemo();
            demo.setSno(i);
            demo.setSname("lucy"+i);
            list.add(demo);
        }
        return list;
    }
}
