package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.user.dao.StudentInfoDao;
import com.qgexam.user.dao.TeacherInfoDao;
import com.qgexam.user.pojo.PO.StudentInfo;
import com.qgexam.user.pojo.VO.GetCourseListVO;
import com.qgexam.user.pojo.VO.GetStudentVO;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.user.dao.*;
import com.qgexam.user.pojo.DTO.*;
import com.qgexam.user.pojo.PO.OptionInfo;
import com.qgexam.user.pojo.PO.QuestionInfo;
import com.qgexam.user.pojo.PO.SubQuestionInfo;
import com.qgexam.user.pojo.VO.ChapterInfoListVO;
import com.qgexam.user.pojo.VO.GetTeacherListVO;
import com.qgexam.user.pojo.VO.OptionInfoVO;
import com.qgexam.user.pojo.VO.SchoolInfoVO;
import com.qgexam.user.service.AdminInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import java.util.ArrayList;
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
    private StudentInfoDao studentInfoDao;

    @Autowired
    private ChapterInfoDao chapterInfoDao;

    @Autowired
    private SchoolInfoDao schoolInfoDao;

    @Autowired
    private SubjectInfoDao subjectInfoDao;

    @Autowired
    private QuestionInfoDao questionInfoDao;

    @Autowired
    private SubQuestionInfoDao subQuestionInfoDao;

    @Autowired
    private OptionInfoDao optionInfoDao;

    /**
     * @author ljy
     * @description 设置教务老师
     * @date 2022/1/7 20:01:03
     */
    @Override
    public boolean addNeteacher(Integer userId) {
        if (teacherInfoDao.insertNeteacher(3, "neteacher", userId) != 0) {
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
    public IPage<GetTeacherListVO> getTeacherList(Integer currentPage, Integer pageSize, Integer schoolId, Integer roleId, String loginName) {
        IPage<GetTeacherListVO> page = new Page<>(currentPage, pageSize);
        return teacherInfoDao.getTeacherPage(schoolId, roleId, loginName, page);
    }

    @Override
    public IPage<GetStudentVO> getStudentList(Integer schoolId, String loginName, Integer currentPage, Integer pageSize) {
        IPage<GetStudentVO> page=new Page<>(currentPage,pageSize);
        return studentInfoDao.getStudentList(schoolId,loginName,page);
    }

    @Override
    public boolean updateStudentNumber(Integer studentId, String newStudentNumber) {
        if(studentInfoDao.updateStudentNumber(studentId,newStudentNumber)!=0&&studentInfoDao.updateStudentNumberInStudentCourse(studentId,newStudentNumber)!=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTeacherNumber(Integer teacherId, String newTeacherNumber) {
       if(teacherInfoDao.updateTeacherNumberInteger(teacherId,newTeacherNumber)!=0){
           return true;
       }
       return false;
    }

    @Override
    public List<ChapterInfoListVO> getChapterBySubjectId(Integer subjectId) {
        List<ChapterInfoListVO> chapterInfoListVOS = chapterInfoDao.getChapterListBySubject(subjectId);
        return chapterInfoListVOS;
    }

    @Override
    public IPage<SchoolInfoVO> getSchoolList(GetSchoolListDTO getSchoolListDTO) {
        IPage<SchoolInfoVO> page = new Page<>(getSchoolListDTO.getCurrentPage(), getSchoolListDTO.getPageSize());
        return schoolInfoDao.getSchoolList(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addQuestion(AddQuestionListDTO addQuestionListDTO) {
        List<AddQuestionDTO> questionDTOList = addQuestionListDTO.getQuestion();
        // 过滤出单选题、多选题、判断题
        List<AddQuestionDTO> chooseQuestionList = questionDTOList.stream()
                .filter(addQuestionDTO ->
                        ExamConstants.QUESTION_TYPE_SINGLE.equals(addQuestionDTO.getType())
                                || ExamConstants.QUESTION_TYPE_MULTI.equals(addQuestionDTO.getType())
                                || ExamConstants.QUESTION_TYPE_JUDGE.equals(addQuestionDTO.getType()))
                .collect(Collectors.toList());
        // 过滤出填空题
        List<AddQuestionDTO> completionQuestionList = questionDTOList.stream()
                .filter(addQuestionDTO ->
                        ExamConstants.QUESTION_TYPE_COMPLETION.equals(addQuestionDTO.getType()))
                .collect(Collectors.toList());
        // 过滤出大题
        List<AddQuestionDTO> complexQuestionList = questionDTOList.stream()
                .filter(addQuestionDTO ->
                        ExamConstants.QUESTION_TYPE_COMPLEX.equals(addQuestionDTO.getType()))
                .collect(Collectors.toList());
        // 保存单选题、多选题、判断题
        chooseQuestionList
                .forEach(questionDTO -> {
                    QuestionInfo questionInfo = new QuestionInfo();
                    questionInfo.setSubjectId(questionDTO.getSubjectId());
                    questionInfo.setChapterId(questionDTO.getChapterId());
                    questionInfo.setSubjectName(questionDTO.getSubjectName());
                    questionInfo.setChapterName(questionDTO.getChapterName());
                    questionInfo.setType(questionDTO.getType());
                    questionInfo.setDescription(questionDTO.getDescription());
                    questionInfo.setDifficultyLevel(questionDTO.getDifficultyLevel());
                    questionInfo.setQuestionAns(questionDTO.getQuestionAns());
                    questionInfo.setSubOrObj(ExamConstants.QUESTION_OBJ);
                    questionInfo.setHasSubQuestion(ExamConstants.NO_SUB_QUESTION);
                    // 保存题目
                    questionInfoDao.insertQuestionInfo(questionInfo);
                    // 获取主键
                    Integer questionInfoKey = questionInfo.getQuestionId();
                    // 获取题目选项
                    List<OptionInfoDTO> optionInfoDTOList = questionDTO.getOptionInfo();
                    // 选项DTO转换成PO,获取PO的list
                    List<OptionInfo> optionInfoList = optionInfoDTOList.stream()
                            .map(optionInfoDTO -> {
                                OptionInfo optionInfo = new OptionInfo();
                                optionInfo.setQuestionId(questionInfoKey);
                                optionInfo.setOptionName(optionInfoDTO.getOptionName());
                                optionInfo.setOptionDesc(optionInfoDTO.getOptionDesc());
                                optionInfo.setOptionId(questionInfoKey);
                                return optionInfo;
                            }).collect(Collectors.toList());

                    // 批量插入选项
                    optionInfoDao.insertOptionInfoBatch(optionInfoList);

                });
        // 保存填空题
        completionQuestionList
                .forEach(questionDTO -> {
                    QuestionInfo questionInfo = new QuestionInfo();
                    questionInfo.setSubjectId(questionDTO.getSubjectId());
                    questionInfo.setChapterId(questionDTO.getChapterId());
                    questionInfo.setSubjectName(questionDTO.getSubjectName());
                    questionInfo.setChapterName(questionDTO.getChapterName());
                    questionInfo.setType(questionDTO.getType());
                    questionInfo.setDescription(questionDTO.getDescription());
                    questionInfo.setDifficultyLevel(questionDTO.getDifficultyLevel());
                    questionInfo.setQuestionAns(questionDTO.getQuestionAns());
                    questionInfo.setSubOrObj(ExamConstants.QUESTION_SUB);
                    questionInfo.setHasSubQuestion(ExamConstants.NO_SUB_QUESTION);
                    // 保存题目
                    questionInfoDao.insertQuestionInfo(questionInfo);
                });
        // 保存大题
        complexQuestionList
                .forEach(questionDTO -> {
                    QuestionInfo questionInfo = new QuestionInfo();
                    questionInfo.setSubjectId(questionDTO.getSubjectId());
                    questionInfo.setChapterId(questionDTO.getChapterId());
                    questionInfo.setSubjectName(questionDTO.getSubjectName());
                    questionInfo.setChapterName(questionDTO.getChapterName());
                    questionInfo.setType(questionDTO.getType());
                    questionInfo.setDescription(questionDTO.getDescription());
                    questionInfo.setDifficultyLevel(questionDTO.getDifficultyLevel());
                    questionInfo.setQuestionAns(questionDTO.getQuestionAns());
                    questionInfo.setSubOrObj(ExamConstants.QUESTION_SUB);
                    if (questionDTO.getSubQuestionInfo() == null || questionDTO.getSubQuestionInfo().isEmpty()) {
                        questionInfo.setHasSubQuestion(ExamConstants.NO_SUB_QUESTION);
                    } else {
                        questionInfo.setHasSubQuestion(ExamConstants.HAS_SUB_QUESTION);
                    }
                    // 保存题目,返回题目的主键
                    questionInfoDao.insertQuestionInfo(questionInfo);
                    // 获取主键
                    Integer questionKey = questionInfo.getQuestionId();
                    // 获取题目小题
                    List<SubQuestionInfoDTO> subQuestionInfoDTOList = questionDTO.getSubQuestionInfo();
                    // 小题DTO转换成PO，获取PO的list
                    List<SubQuestionInfo> subQuestionInfoList = subQuestionInfoDTOList.stream()
                            .map(subQuestionInfoDTO -> {
                                SubQuestionInfo subQuestionInfo = new SubQuestionInfo();
                                subQuestionInfo.setQuestionId(questionKey);
                                subQuestionInfo.setSubQuestionDesc(subQuestionInfoDTO.getSubQuestionDesc());
                                subQuestionInfo.setSubQuestionAns(subQuestionInfoDTO.getSubQuestionAns());
                                return subQuestionInfo;
                            })
                            .collect(Collectors.toList());
                    // 批量插入小题
                    subQuestionInfoDao.insertSubQuestionInfoBatch(subQuestionInfoList);
                });

    }
}
