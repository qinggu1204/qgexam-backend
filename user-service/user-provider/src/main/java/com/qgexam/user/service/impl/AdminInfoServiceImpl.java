package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.user.dao.*;
import com.qgexam.user.pojo.DTO.AddQuestionDTO;
import com.qgexam.user.pojo.DTO.AddQuestionListDTO;
import com.qgexam.user.pojo.DTO.GetSchoolListDTO;
import com.qgexam.user.pojo.DTO.OptionInfoDTO;
import com.qgexam.user.pojo.PO.OptionInfo;
import com.qgexam.user.pojo.PO.QuestionInfo;
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
    @Transactional
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
                    Integer questionInfoKey = questionInfoDao.insertQuestionInfo(questionInfo);
                    // 获取题目选项
                    List<OptionInfoDTO> optionInfoDTOList = questionDTO.getOptionInfo();
                    // DTO转换成PO
//                    List<OptionInfo> optionInfoList = optionInfo.stream()
//                            .map(optionInfoDTO -> {
//                                OptionInfo optionInfoPO = new OptionInfo();
//                                optionInfoPO.setQuestionId(questionInfoKey);
//                                optionInfoPO.setOptionContent(optionInfoDTO.getOptionContent());
//                                optionInfoPO.setOptionAns(optionInfoDTO.getOptionAns());
//                                return optionInfoPO;
//                            })
//                            .collect(Collectors.toList());
//                    // 为题目选项设置题目id
//                    optionInfo.forEach(optionInfoVO -> optionInfoVO.setQuestionId(questionInfoKey));
//                    optionInfoDao.insertOptionInfoBatch(optionInfo, questionInfoKey);
                    // TODO 保存题目选项
                });
    }

}
