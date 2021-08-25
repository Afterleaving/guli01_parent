package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author lwl
 * @since 2021-08-08
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Resource
    private EduVideoService videoService;

    //根据课程id查询课程大纲列表
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        List<ChapterVo> finalList = new ArrayList<>();
        //1.根据课程id查询章节
        QueryWrapper<EduChapter> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id",courseId);
        List<EduChapter> chapterList = baseMapper.selectList(wrapper1);

        //2.根据课程id查询小节
        QueryWrapper<EduVideo> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id",courseId);
        List<EduVideo> videoList = videoService.list(wrapper2);

        //3.封装数据
        for (EduChapter chapter : chapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            List<VideoVo> videoVoList = new ArrayList<>();
            for (EduVideo video : videoList) {
                if (video.getChapterId().equals(chapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);
            finalList.add(chapterVo);
        }

        return finalList;
    }

    //删除章节，如果章节里面有小节则不能删除
    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        if (count > 0){ //查询出小节，不能删除
            throw new GuliException(20001,"不能删除");
        } else {    //不能查询出数据
            return baseMapper.deleteById(chapterId) > 0;    //成功 1 > 0   失败 0 > 0
        }
    }

    //2.根据课程id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id",courseId);
        baseMapper.delete(wrapper2);
    }

}
