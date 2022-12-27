package com.qgexam.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgexam.user.pojo.PO.ChapterInfo;
import com.qgexam.user.pojo.VO.ChapterInfoListVO;

import java.util.List;

/**
 * 学科章节表(ChapterInfo)表数据库访问层
 *
 * @author ljy
 * @since 2022-12-25 22:51:46
 */
public interface ChapterInfoDao extends BaseMapper<ChapterInfo> {
    List<ChapterInfoListVO> getChapterListBySubject(Integer subjectId);
}

