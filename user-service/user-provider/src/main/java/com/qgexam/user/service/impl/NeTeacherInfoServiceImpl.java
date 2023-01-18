package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;


import com.qgexam.quartz.pojo.PO.SysJob;
import com.qgexam.quartz.service.SysJobService;
import com.qgexam.quartz.utils.CronUtil;
import com.qgexam.user.constants.ExamBeginJobConstants;
import com.qgexam.user.constants.ExamFinishJobConstants;
import com.qgexam.user.constants.ExamUnderwayJobConstants;
import com.qgexam.user.dao.*;
import com.qgexam.user.pojo.DTO.CreateExamDTO;
import com.qgexam.user.pojo.DTO.GetInvigilationInfoDTO;
import com.qgexam.user.pojo.PO.*;
import com.qgexam.user.pojo.VO.*;
import com.qgexam.user.service.NeTeacherInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yzw
 * @description
 * @date 2022/12/21 20:01:03
 */
@DubboService
public class NeTeacherInfoServiceImpl implements NeTeacherInfoService {
    @Autowired
    private TeacherInfoDao teacherInfoDao;
    @Autowired
    private ExaminationInfoDao examinationInfoDao;
    @Autowired
    private ChapterInfoDao chapterInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private AnswerPaperInfoDao answerPaperInfoDao;
    @Autowired
    private ExaminationPaperDao examinationPaperDao;
    @Autowired
    private OptionInfoDao optionInfoDao;
    @Autowired
    private SubQuestionInfoDao subQuestionInfoDao;

    @DubboReference(registry = "quartzRegistry")
    private SysJobService sysJobService;

    /**
     * 查看试卷列表
     */
    @Override
    public IPage<GetExaminationPaperVO> getExaminationPaperList(SaSession session, Integer currentPage, Integer pageSize) {
        IPage<GetExaminationPaperVO> page = new Page<>(currentPage, pageSize);
        /*获取用户信息*/
        UserInfoVO userInfoVO = (UserInfoVO) session.get(SystemConstants.SESSION_USER_KEY);
        List<GetExaminationPaperVO> getExaminationPaperVOList = teacherInfoDao.getExaminationPaperList(userInfoVO.getUserInfo().getUserId());
        page.setRecords(getExaminationPaperVOList);
        return page.convert(examinationPaper -> BeanCopyUtils.copyBean(examinationPaper, GetExaminationPaperVO.class));
    }

    /**
     * 安排监考教师
     */
    @Override
    @Transactional
    public boolean arrangeInvigilation(Integer examinationId) {
        boolean flag = true;
        /*获取可以安排的教师列表*/
        List<TeacherInfo> availableTeacherList = teacherInfoDao.getInvigilationTeacherList(examinationId);
        /*获取参加该场考试的课程列表*/
        List<CourseInfo> availableCourseList = teacherInfoDao.getCourseExaminationList(examinationId);
        /*获取考试名*/
        String examinationName = examinationInfoDao.getByExaminationId(examinationId).getExaminationName();
        /*随机抽取列表中的教师作为监考教师*/
        Random rand = new Random();
        int randomIndex = rand.nextInt(availableTeacherList.size());
        for (CourseInfo course : availableCourseList) {
            for (int i = 0; i < availableCourseList.size(); i++) {
                /*随机抽中的教师信息*/
                TeacherInfo randomElement = availableTeacherList.get(randomIndex);
                /*创建该教师收到的监考消息对象*/
                MessageInfo messageInfo = new MessageInfo();
                /*注入消息对象属性值*/
                messageInfo.setUserId(randomElement.getUserId());
                messageInfo.setTitle("监考通知");
                messageInfo.setExaminationName(examinationName);
                messageInfo.setStartTime(examinationInfoDao.getByExaminationId(examinationId).getStartTime());
                messageInfo.setEndTime(examinationInfoDao.getByExaminationId(examinationId).getEndTime());
                if (teacherInfoDao.arrangeInvigilation(examinationId, course.getCourseId(), randomElement.getTeacherId(), course.getCourseName(), randomElement.getUserName(), examinationName) == 0) {
                    flag = false;
                }
                messageInfoDao.insertMessageInfo(messageInfo);
                availableTeacherList.remove(randomIndex);
            }
        }
        if (flag) return true;
        else return false;
    }

    /**
     * @author ljy
     * @description 获取学科章节列表
     * @date 2022/12/25 20:01:03
     */
    @Override
    public List<ChapterInfoListVO> getChapterInfoList(Integer subjectId) {
        return chapterInfoDao.getChapterListBySubject(subjectId);
    }

    /**
     * @author ljy
     * @description 创建（发布）考试
     * @date 2022/12/28 22:01:03
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createExamination(Integer userId, CreateExamDTO createExamDTO)  {
        /*1.创建新考试对象*/
        ExaminationInfo examinationInfo = new ExaminationInfo();
        examinationInfo.setExaminationPaperId(createExamDTO.getExaminationPaperId());
        examinationInfo.setExaminationName(createExamDTO.getExaminationName());
        examinationInfo.setCreatedBy(userId);
        examinationInfo.setStartTime(createExamDTO.getStartTime());
        examinationInfo.setEndTime(createExamDTO.getEndTime());
        examinationInfo.setLimitTime(createExamDTO.getLimitTime());
        examinationInfo.setIsQuestionResort(createExamDTO.getIsQuestionResort());
        examinationInfo.setIsOptionResort(createExamDTO.getIsOptionResort());
        /*2.向数据表插入考试并获取该考试的examinationId*/
        if (examinationInfoDao.insertExaminationInfo(examinationInfo) == 0) {
            return false;
        }
        Integer examinationId = examinationInfo.getExaminationId();
        // ---------------------------yzw添加开始---------------------------------
        // 创建定时任务，在考试开始前10分钟将试卷信息放入redis
        // 不设置Status，因为定时任务默认为启用状态 不设置concurrent，因为定时任务默认为不允许并发执行
        // 不设置misfirePolicy 因为定时任务失火时默认为放弃执行
        SysJob job = new SysJob()
                // 定时任务的名称为examBeginJob :考试Id:考试名称
                .setJobName(ExamBeginJobConstants.JOB_NAME + ":" + examinationId)
                .setJobGroup(ExamBeginJobConstants.JOB_GROUP)
                .setInvokeTarget(ExamBeginJobConstants.getInvokeTarget(examinationId));
        // 获取考试开始时间
        LocalDateTime startTime = examinationInfo.getStartTime();
        // hutool日期偏移，获取startTime的前10分钟
        LocalDateTime jobStartTime = LocalDateTimeUtil.offset(startTime, ExamBeginJobConstants.TIME_BEGIN_EXAM, ChronoUnit.MINUTES);
        // 根据上述时间获取cron表达式
        String cron = CronUtil.localDateTimeToCron(jobStartTime);
        // 设置cron表达式
        job.setCronExpression(cron);
        // 添加job
        Boolean succ = null;
        try {
            succ = sysJobService.saveJob(job);
        } catch (SchedulerException e) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        if (!succ) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "创建考试失败");
        }
        // ---------------------------yzw添加结束---------------------------------

        // ---------------------------修改考试状态为进行中的定时任务---------------------------------
        SysJob job1 = new SysJob()
                // 定时任务的名称为examBeginJob :考试Id:考试名称
                .setJobName(ExamUnderwayJobConstants.JOB_NAME + ":" + examinationId)
                .setJobGroup(ExamUnderwayJobConstants.JOB_GROUP)
                .setInvokeTarget(ExamUnderwayJobConstants.getInvokeTarget(examinationId));
        // 根据上述时间获取cron表达式
        String cron1 = CronUtil.localDateTimeToCron(createExamDTO.getStartTime());
        // 设置cron表达式
        job1.setCronExpression(cron1);
        // 添加job
        Boolean succ1 = null;
        try {
            succ1 = sysJobService.saveJob(job1);
        } catch (SchedulerException e) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        if (!succ1) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "创建考试失败");
        }

        // ---------------------------修改考试状态为已结束的定时任务---------------------------------
        SysJob job2 = new SysJob()
                // 定时任务的名称为examBeginJob :考试Id:考试名称
                .setJobName(ExamFinishJobConstants.JOB_NAME + ":" + examinationId)
                .setJobGroup(ExamFinishJobConstants.JOB_GROUP)
                .setInvokeTarget(ExamFinishJobConstants.getInvokeTarget(examinationId));
        // 根据上述时间获取cron表达式
        String cron2 = CronUtil.localDateTimeToCron(createExamDTO.getEndTime());
        // 设置cron表达式
        job2.setCronExpression(cron2);
        // 添加job
        Boolean succ2 = null;
        try {
            succ2 = sysJobService.saveJob(job2);
        } catch (SchedulerException e) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        if (!succ2) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "创建考试失败");
        }

        /*3.根据学科编号获取课程列表*/
        List<CourseInfo> courseList = courseInfoDao.getCourseListBySubject(createExamDTO.getSubjectId());
        /*4.给这些课程及其学生发布考试*/
        for (CourseInfo course : courseList) {
            /*4.1.插入课程考试关联表*/
            if (teacherInfoDao.insertCourseExamination(examinationId, course.getCourseId()) == 0) {
                return false;
            }
            /*4.2.获取该课程的学生列表*/
            List<StudentInfo> studentInfoList = teacherInfoDao.getStudentListByCourse(course.getCourseId());
            /*4.3.给学生发布考试通知*/
            for (StudentInfo student : studentInfoList) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setUserId(student.getUserId());
                messageInfo.setTitle("考试通知");
                messageInfo.setExaminationName(createExamDTO.getExaminationName());
                messageInfo.setStartTime(createExamDTO.getStartTime());
                messageInfo.setEndTime(createExamDTO.getEndTime());
                AnswerPaperInfo answerPaperInfo = new AnswerPaperInfo();
                answerPaperInfo.setStudentId(student.getStudentId());
                answerPaperInfo.setExaminationId(examinationId);
                answerPaperInfo.setExaminationName(createExamDTO.getExaminationName());
                if (messageInfoDao.insertMessageInfo(messageInfo) == 0 || answerPaperInfoDao.insert(answerPaperInfo) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public IPage<GetInvigilationInfoVO> getInvigilationInfo(GetInvigilationInfoDTO getInvigilationInfoDTO) {
        IPage<GetInvigilationInfoVO> page = new Page<>(getInvigilationInfoDTO.getCurrentPage(), getInvigilationInfoDTO.getPageSize());
        return teacherInfoDao.selectInvigilationInfo(page, getInvigilationInfoDTO.getExaminationId());
    }



    @Override
    public List<PreviewQuestionInfoVO> previewPaper(Integer examinationPaperId) {
        // 查询试卷，同时查询试卷中的题目
        ExaminationPaper examinationPaper = examinationPaperDao.selectExaminationPaperById(examinationPaperId);
        // 获取题目list
        List<QuestionInfo> questionInfoList = examinationPaper.getQuestionInfoList();
        questionInfoList.stream()
                .forEach(questionInfo -> {
                    // 获取题目Id
                    Integer questionId = questionInfo.getQuestionId();
                    // 根据题目Id查询选项
                    List<PreviewOptionInfoVO> optionInfos = optionInfoDao.selectPreviewOptionInfoListByQuestionInfoId(questionId);
                    // 根据题目Id查询小题
                    List<PreviewSubQuestionInfoVO> subQuestionInfos = subQuestionInfoDao.selectPreviewSubQuestionInfoListByQuestionInfoId(questionId);
                    questionInfo.setPreviewOptionInfo(optionInfos);
                    questionInfo.setPreviewSubQuestionInfo(subQuestionInfos);
                });
        // 从questionInfoList过滤出不同题型
        List<QuestionInfo> singleQuestionInfoList = questionInfoList.stream()
                .filter(questionInfo -> ExamConstants.QUESTION_TYPE_SINGLE.equals(questionInfo.getType()))
                .collect(Collectors.toList());

        List<QuestionInfo> multipleQuestionInfoList = questionInfoList.stream()
                .filter(questionInfo -> ExamConstants.QUESTION_TYPE_MULTI.equals(questionInfo.getType()))
                .collect(Collectors.toList());

        List<QuestionInfo> judgeQuestionInfoList = questionInfoList.stream()
                .filter(questionInfo -> ExamConstants.QUESTION_TYPE_JUDGE.equals(questionInfo.getType()))
                .collect(Collectors.toList());

        List<QuestionInfo> completionQuestionInfoList = questionInfoList.stream()
                .filter(questionInfo -> ExamConstants.QUESTION_TYPE_COMPLETION.equals(questionInfo.getType()))
                .collect(Collectors.toList());

        List<QuestionInfo> complexQuestionInfoList = questionInfoList.stream()
                .filter(questionInfo -> ExamConstants.QUESTION_TYPE_COMPLEX.equals(questionInfo.getType()))
                .collect(Collectors.toList());

        List<QuestionInfo> questionInfos = new ArrayList<>();
        questionInfos.addAll(singleQuestionInfoList);
        questionInfos.addAll(multipleQuestionInfoList);
        questionInfos.addAll(judgeQuestionInfoList);
        questionInfos.addAll(completionQuestionInfoList);
        questionInfos.addAll(complexQuestionInfoList);
        List<PreviewQuestionInfoVO> previewQuestionInfoVOList = questionInfos.stream()
                .map(questionInfo -> {
                    PreviewQuestionInfoVO previewQuestionInfoVO = BeanCopyUtils.copyBean(questionInfo, PreviewQuestionInfoVO.class);
                    previewQuestionInfoVO.setOptionInfo(questionInfo.getPreviewOptionInfo());
                    previewQuestionInfoVO.setSubQuestionInfo(questionInfo.getPreviewSubQuestionInfo());
                    return previewQuestionInfoVO;
                })
                .collect(Collectors.toList());

        return previewQuestionInfoVOList;
    }
}
