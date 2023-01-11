package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.user.dao.ChapterInfoDao;
import com.qgexam.user.dao.SchoolInfoDao;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.pojo.DTO.AddQuestionDTO;
import com.qgexam.user.pojo.DTO.AddQuestionListDTO;
import com.qgexam.user.pojo.DTO.GetSchoolListDTO;
import com.qgexam.user.pojo.VO.ChapterInfoListVO;
import com.qgexam.user.pojo.VO.GetTeacherListVO;
import com.qgexam.user.pojo.VO.SchoolInfoVO;
import com.qgexam.user.service.AdminInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author ljy
 * @description 管理员服务接口
 * @date 2022/1/6 20:00:44
 */
@DubboService
public class AdminInfoServiceImpl implements AdminInfoService {
    @Autowired
    private TeacherInfoDao teacherInfoDao;

    @Autowired
    private ChapterInfoDao chapterInfoDao;

    @Autowired
    private SchoolInfoDao schoolInfoDao;

    /**
     * @author ljy
     * @description 设置教务老师
     * @date 2022/1/7 20:01:03
     */
    @Override
    public boolean addNeteacher(Integer userId){
        if (teacherInfoDao.insertNeteacher(3,"neteacher",userId) != 0) {
            return true;
        }
        return false;
    }

    /**
     * @author ljy
     * @description 管理员查看任课/教务教师列表
     * @date 2022/1/7 20:01:03
     */
    @Override
    public IPage<GetTeacherListVO> getTeacherList(Integer currentPage, Integer pageSize, Integer schoolId, Integer roleId, String loginName){
        IPage<GetTeacherListVO> page=new Page<>(currentPage,pageSize);
        return teacherInfoDao.getTeacherPage(schoolId,roleId,loginName,page);
    }

    @Override
    public List<ChapterInfoListVO> getChapterBySubjectId(Integer subjectId) {
        List<ChapterInfoListVO> chapterInfoListVOS = chapterInfoDao.getChapterListBySubject(subjectId);
        return chapterInfoListVOS;
    }

    @Override
    public IPage<SchoolInfoVO> getSchoolList(GetSchoolListDTO getSchoolListDTO) {
        IPage<SchoolInfoVO> page=new Page<>(getSchoolListDTO.getCurrentPage(),getSchoolListDTO.getPageSize());
        return schoolInfoDao.getSchoolList(page);
    }

    @Override
    public void addQuestion(AddQuestionListDTO addQuestionListDTO) {
        List<AddQuestionDTO> questionDTOList = addQuestionListDTO.getQuestion();
        // TODO 完成题目的插入
    }

}
