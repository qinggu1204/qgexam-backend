package com.qgexam.user.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.SystemConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;


import com.qgexam.quartz.pojo.PO.SysJob;
import com.qgexam.quartz.service.SysJobService;
import com.qgexam.quartz.utils.CronUtil;
import com.qgexam.user.constants.ExamBeginJobConstants;
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
import java.util.List;
import java.util.Random;

/**
 * @author yzw
 * @description
 * @date 2022/12/21 20:01:03
 */
@DubboService
@Transactional
public class NeTeacherInfoServiceImpl implements NeTeacherInfoService {
    @Autowired
    private TeacherInfoDao teacherInfoDao;

    @Autowired
    private ExaminationPaperDao examinationPaperDao;

    @Autowired
    private ChapterInfoDao chapterInfoDao;

    @Autowired
    private CourseInfoDao courseInfoDao;
    
    @Autowired
    private QuestionInfoDao questionInfoDao;

    @Autowired
    private SubjectInfoDao subjectInfoDao;

    @Autowired
    private AnswerPaperInfoDao answerPaperInfoDao;

    @Autowired
    private ExaminationInfoDao examinationInfoDao;

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
    private StudentInfoDao studentInfoDao;

    @Autowired
    private AnswerPaperDetailDao answerPaperDetailDao;

    @Autowired
    private RedisCache redisCache;

    @DubboReference(registry = "quartzRegistry")
    private SysJobService sysJobService;


    /**
     * @param teacherId
     * @param createPaperDTO
     * @return void
     * @description 教务教师组卷，向试卷表插入记录，根据题目总数量和章节重要程度按比例分配章节题目数量，并按照题目平均难度随机选取题目，插入试卷-题目表
     * @author peter guo
     * @date 2022/12/25 21:09:00
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPaper(Integer teacherId, CreatePaperDTO createPaperDTO) {
        //将相关信息插入试卷表
        ExaminationPaper examinationPaper = new ExaminationPaper();
        examinationPaper.setTitle(createPaperDTO.getTitle());
        examinationPaper.setTotalScore(createPaperDTO.getTotalScore());
        examinationPaper.setCreatedBy(teacherId);
        //空试卷插入试卷表
        int count = examinationPaperDao.insert(examinationPaper);
        if (count < 1) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //获取创建的空试卷的id
        Integer examinationPaperId = examinationPaper.getExaminationPaperId();

        //根据学科编号和章节编号查询章节重要程度
        LambdaQueryWrapper<ChapterInfo> queryWrapper = new LambdaQueryWrapper<ChapterInfo>();
        queryWrapper.eq(ChapterInfo::getSubjectId, createPaperDTO.getSubjectId())
                .in(ChapterInfo::getChapterId, createPaperDTO.getChapterList());
        List<ChapterInfo> chapterInfoList = chapterInfoDao.selectList(queryWrapper);
        if (chapterInfoList.isEmpty()) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        List<ChapterImportanceLevelVO> chapterImportanceLevelVOS =
                BeanCopyUtils.copyBeanList(chapterInfoList, ChapterImportanceLevelVO.class);

        //按照重要程度和平均难度分配题目
        List<QuestionDTO> questionDetail = createPaperDTO.getQuestionDetail();
        questionDetail.forEach(questionDTO -> {
            //获取题目类型
            String questionType = questionDTO.getQuestionType();
            //获取题目数量
            Integer questionNum = questionDTO.getQuestionNumber();
            //获取题目总分
            Integer questionTotalScore = questionDTO.getQuestionTotalScore();
            //获取题目平均难度
            Integer difficultyLevel = questionDTO.getDifficultyLevel();

            //计算出每一题的分数
            double questionScore = (double) questionTotalScore / questionNum;
            //保留两位小数
            questionScore = (double) Math.round(questionScore * 100) / 100;

            //根据题目数量和平均难度生成随机数，随机数个数为题目数量，随机数的平均值为平均难度
            double percent;
            int min = ExamConstants.DIFFICULTY_LEVEL_MINIMUM;//随机数最小值
            int max = ExamConstants.DIFFICULTY_LEVEL_MAXIMUM;//随机数最大值
            percent = (double) (max - difficultyLevel) / (max - min);

            Random random = new Random();
            int questionDifficultyLevel;//题目难度
            LinkedList<Integer> list = new LinkedList<>();//存放难度随机数的链表
            int total;//生成的随机数的和
            while (true) {
                int i = questionNum;
                total = list.stream().mapToInt(Integer::intValue).sum();
                //如果计算所得随机数的平均值不为0且与标准平均难度的差的绝对值小于0.5，跳出循环
                if (total != 0 && Math.abs((double) total / questionNum - difficultyLevel) < 0.5) {
                    break;
                }
                //若精度不符合要求，先清空链表，重新生成随机数
                list.clear();
                while (i > 0) {
                    if (random.nextDouble() < percent) {
                        //在[min,平均难度]上生成随机数
                        questionDifficultyLevel = min + random.nextInt(difficultyLevel - min + 1);
                    } else {
                        //在[平均难度+1,max]上生成随机数
                        questionDifficultyLevel = difficultyLevel + 1 + random.nextInt(max - difficultyLevel);
                    }
                    list.add(questionDifficultyLevel);
                    i--;
                }
            }

            //将章节重要程度作为权重随机分配章节题目数量
            List<WeightRandom.WeightObj<Integer>> weightObjList = new ArrayList<WeightRandom.WeightObj<Integer>>();
            //将章节id和重要程度作为权重对象存入列表
            chapterImportanceLevelVOS.forEach(chapterImportanceLevelVO -> {
                WeightRandom.WeightObj<Integer> weightObj =
                        new WeightRandom.WeightObj<Integer>(chapterImportanceLevelVO.getChapterId(),
                                chapterImportanceLevelVO.getImportanceLevel());
                weightObjList.add(weightObj);
            });
            //创建带有权重的随机生成器
            WeightRandom<Integer> wr = RandomUtil.weightRandom(weightObjList);

            //创建一个map，用于存放每一章节的题目数量
            Map<Integer, Integer> chapterQuestionNumMap = new HashMap<>();
            //初始化map
            chapterImportanceLevelVOS.forEach(chapterImportanceLevelVO -> {
                chapterQuestionNumMap.put(chapterImportanceLevelVO.getChapterId(), 0);
            });

            //生成所需题目个数个随机数
            for (int i = 0; i < questionNum; i++) {
                //根据权重随机生成章节id
                Integer chapterId = wr.next();
                chapterQuestionNumMap.put(chapterId, chapterQuestionNumMap.get(chapterId) + 1);
            }

            //遍历map
            for (Integer chapterId : chapterQuestionNumMap.keySet()) {
                //获取每一章节的题目数量
                Integer questionNumInChapter = chapterQuestionNumMap.get(chapterId);
                //如果章节的题目数量为0，跳过
                if (questionNumInChapter == 0) {
                    continue;
                }
                //题目编号列表
                List<Integer> questionIdList = new ArrayList<Integer>();
                //题目编号
                Integer questionId;

                //将对应数量的题目编号存入列表中
                while (questionIdList.size() != questionNumInChapter) {//如果数量不足分配的数量
                    //根据学科编号、章节编号、题目类型、题目难度随机查询题目,取题目编号并记录下来
                    LambdaQueryWrapper<QuestionInfo> questionInfoQueryWrapper = new LambdaQueryWrapper<QuestionInfo>();
                    questionInfoQueryWrapper.select(QuestionInfo::getQuestionId)
                            .eq(QuestionInfo::getSubjectId, createPaperDTO.getSubjectId())
                            .eq(QuestionInfo::getChapterId, chapterId)
                            .eq(QuestionInfo::getType, questionType)
                            .eq(QuestionInfo::getDifficultyLevel, list.pop())
                            .last("order by RAND() LIMIT 1");
                    questionId = questionInfoDao.selectOne(questionInfoQueryWrapper).getQuestionId();
                    //先查询列表中有没有该题目编号，如果有，就重新查询数据库
                    while (questionIdList.contains(questionId)) {
                        questionId = questionInfoDao.selectOne(questionInfoQueryWrapper).getQuestionId();
                    }
                    questionIdList.add(questionId);
                }

                //将题目和考试编号批量插入试卷题目关联表
                Integer insertCount = examinationPaperDao.savePaperQuestionBatch(examinationPaperId, questionIdList, questionScore);
                if (insertCount < 1) {
                    throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
                }
            }
        });
    }

    /**
     * @return java.util.List<com.qgexam.user.pojo.VO.SubjectVO>
     * @description 获取学科列表
     * @author peter guo
     * @date 2022/12/25 21:09:25
     */
    @Override
    public List<SubjectVO> getSubjectList() {
        List<SubjectInfo> subjectInfos = subjectInfoDao.selectList(null);
        return BeanCopyUtils.copyBeanList(subjectInfos, SubjectVO.class);
    }

    /**
     * @param examinationId
     * @description 教务教师分配阅卷任务
     * @author peter guo
     * @date 2022/12/25 22:13:18
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributeJudgeTask(Integer examinationId, Date endTime) {
        //查询该考试的主观题编号
        ExaminationInfo examinationInfo = examinationInfoDao.selectById(examinationId);
        List<Integer> subQuestionIdList = examinationInfoDao.selectSubQuestionIdList(examinationInfo.getExaminationPaperId());
        //若没有主观题则不需用到教师阅卷，直接抛出异常
        if (subQuestionIdList.isEmpty()) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"无主观题无需教师阅卷");
        }
        //查询作弊的学生的答卷编号
        List<Integer> cheatPaperIdList = answerPaperInfoDao.getCheatPaperIdList(examinationId);
        //根据考试编号查询教这个学科的教师编号列表
        List<Integer> teacherIdList = examinationInfoDao.getTeacherIdList(examinationId);

        //如果教师编号列表为空，抛出异常
        if (teacherIdList.isEmpty()){
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //查询没有作弊的学生的答卷编号
        LambdaQueryWrapper<AnswerPaperInfo> queryWrapper = new LambdaQueryWrapper<AnswerPaperInfo>();
        queryWrapper.select(AnswerPaperInfo::getAnswerPaperId)
                .eq(AnswerPaperInfo::getExaminationId,examinationId)
                .isNull(AnswerPaperInfo::getTeacherId)
                .notIn(!cheatPaperIdList.isEmpty(),AnswerPaperInfo::getAnswerPaperId, cheatPaperIdList);

        List<Integer> answerPaperIdList = answerPaperInfoDao.selectList(queryWrapper).stream()
                .map(AnswerPaperInfo::getAnswerPaperId)
                .collect(Collectors.toList());

        if(answerPaperIdList.isEmpty()){
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"没有需要分配的答卷");
        }

        List<Integer> tmp = new ArrayList<>();
        //将白卷剔除，不分配阅卷任务
        answerPaperIdList.forEach(answerPaperId -> {
            LambdaQueryWrapper<AnswerPaperDetail> answerPaperDetailQueryWrapper = new LambdaQueryWrapper<AnswerPaperDetail>();
            answerPaperDetailQueryWrapper.eq(AnswerPaperDetail::getAnswerPaperId, answerPaperId);
            Long count = answerPaperDetailDao.selectCount(answerPaperDetailQueryWrapper);
            if (count == 0) {
                tmp.add(answerPaperId);
            }
        });
        answerPaperIdList.removeAll(tmp);

        //将答卷编号随机分配给教师
        //创建权重对象列表
        List<WeightRandom.WeightObj<Integer>> weightObjList = new ArrayList<>();
        //初始化权重对象列表
        teacherIdList.forEach(teacherId -> {
            weightObjList.add(new WeightRandom.WeightObj<>(teacherId, 1));
        });
        //创建带有权重的随机生成器
        WeightRandom<Integer> wr = RandomUtil.weightRandom(weightObjList);

        //创建一个保存教师编号和教师所批试卷编号列表的map
        Map<Integer, List<Integer>> teacherAnswerPapersMap = new HashMap<>();

        //根据教师编号列表初始化map
        teacherIdList.forEach(teacherId -> {
            teacherAnswerPapersMap.put(teacherId, new ArrayList<>());
        });

        //遍历答卷编号列表
        answerPaperIdList.forEach(answerPaperId -> {
            //随机获取教师编号
            Integer teacherId = wr.next();
            //将教师编号和答卷编号列表存入map
            teacherAnswerPapersMap.get(teacherId).add(answerPaperId);
        });
        //排除掉没有答卷的教师
        teacherAnswerPapersMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        //遍历map，更新数据库中答卷编号对应的教师编号，即把答卷分配给该教师
        teacherAnswerPapersMap.forEach((teacherId, teacherAnswerPaperIdList) -> {
            //批量更新答卷编号对应的教师编号
            LambdaUpdateWrapper<AnswerPaperInfo> updateWrapper = new LambdaUpdateWrapper<AnswerPaperInfo>();
            updateWrapper.set(AnswerPaperInfo::getTeacherId, teacherId)
                    .in(AnswerPaperInfo::getAnswerPaperId, teacherAnswerPaperIdList);
            int updateCount = answerPaperInfoDao.update(null, updateWrapper);
            if (updateCount < 1) {
                throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
            }
        });

        //向有阅卷任务的教师发送消息
        List<Integer> teacherIdListWithPaper = teacherAnswerPapersMap.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (teacherIdListWithPaper.isEmpty()){
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"没有需要发送消息的阅卷教师");
        }

        //根据教师id查询userid
        LambdaQueryWrapper<TeacherInfo> teacherInfoQueryWrapper = new LambdaQueryWrapper<TeacherInfo>();
        teacherInfoQueryWrapper.select(TeacherInfo::getUserId)
                .in(TeacherInfo::getTeacherId, teacherIdListWithPaper);
        List<Integer> teacherUserIdList = teacherInfoDao.selectList(teacherInfoQueryWrapper).stream()
                .map(TeacherInfo::getUserId)
                .collect(Collectors.toList());
        if (teacherUserIdList.isEmpty()) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //教师阅卷消息插入消息表
        String examinationName = examinationInfoDao.selectById(examinationId).getExaminationName();
        String title = MessageConstants.MARKING_NOTICE;
        List<MessageInfo> messageInfoList = new ArrayList<>();
        teacherUserIdList.forEach(teacherUserId -> {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setUserId(teacherUserId);
            messageInfo.setTitle(title);
            messageInfo.setExaminationName(examinationName);
            messageInfo.setEndTime(LocalDateTimeUtil.of(endTime));
            messageInfoList.add(messageInfo);
        });
        Integer count = messageInfoDao.insertMarkingMessageBatch(messageInfoList);
        if (count < 1) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //将学生查询成绩通知插入到数据库中
        String scoreQueryTitle = MessageConstants.SCORE_QUERY_NOTICE;

        //根据考试编号查询学生编号列表
        LambdaQueryWrapper<AnswerPaperInfo> answerPaperInfoQueryWrapper = new LambdaQueryWrapper<AnswerPaperInfo>();
        answerPaperInfoQueryWrapper.select(AnswerPaperInfo::getStudentId)
                .eq(AnswerPaperInfo::getExaminationId, examinationId);
        List<Integer> studentIdList = answerPaperInfoDao.selectList(answerPaperInfoQueryWrapper).stream()
                .map(AnswerPaperInfo::getStudentId)
                .collect(Collectors.toList());
        if (studentIdList.isEmpty()) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //根据学生编号列表查询用户编号列表
        LambdaQueryWrapper<StudentInfo> studentInfoQueryWrapper = new LambdaQueryWrapper<StudentInfo>();
        studentInfoQueryWrapper.select(StudentInfo::getUserId)
                .in(StudentInfo::getStudentId, studentIdList);
        List<Integer> studentUserIdList = studentInfoDao.selectList(studentInfoQueryWrapper).stream()
                .map(StudentInfo::getUserId)
                .collect(Collectors.toList());

        if (studentUserIdList.isEmpty()) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //学生查询成绩通知插入消息表
        List<MessageInfo> studentMessageInfoList = new ArrayList<>();
        studentUserIdList.forEach(studentUserId -> {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setUserId(studentUserId);
            messageInfo.setTitle(scoreQueryTitle);
            messageInfo.setExaminationName(examinationName);
            messageInfo.setStartTime(LocalDateTimeUtil.of(endTime));
            studentMessageInfoList.add(messageInfo);
        });
        Integer insertCount = messageInfoDao.insertScoreQueryMessageBatch(studentMessageInfoList);

        if (insertCount < 1) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //将消息的编号拿出来作为列表存入redis中
        List<Integer> messageIdList = studentMessageInfoList.stream()
                .map(MessageInfo::getMessageId)
                .collect(Collectors.toList());
        //将消息编号列表存入redis中
        redisCache.setCacheList(MessageConstants.MESSAGE_ID_LIST_KEY, messageIdList);

        //定时任务，通知答卷可以开始缓存
        SysJob beginCacheJob = new SysJob()
                // 定时任务的名称为beginCacheJob :考试Id
                .setJobName(BeginCacheJobConstants.BEGIN_CACHE_JOB_NAME + ":" + examinationId)
                .setJobGroup(BeginCacheJobConstants.BEGIN_CACHE_JOB_GROUP_NAME)
                .setCronExpression(DateTimeToCronUtils.getCron(endTime,DateTimeToCronUtils.YEAR))
                .setInvokeTarget(BeginCacheJobConstants.getInvokeTarget(examinationId));

        sysJobService.deleteJobById(beginCacheJob);
        Boolean succ;
        try {
            succ = sysJobService.saveJob(beginCacheJob);
        } catch (SchedulerException e) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        if (!succ) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "发送信息失败");
        }

        //延迟一小时发送将学生查询成绩通知置为可查询到
        LocalDateTime noticeTaskTime = LocalDateTimeUtil.offset(LocalDateTimeUtil.of(endTime), 1, ChronoUnit.MINUTES);

        //定时任务，定时将学生查询成绩通知逻辑删除置为0即可以查询到
        SysJob queryScoreNoticeJob = new SysJob()
                // 定时任务的名称为queryScoreNoticeJob
                .setJobName(QueryScoreNoticeJobConstants.QUERY_SCORE_NOTICE_JOB_NAME)
                .setJobGroup(QueryScoreNoticeJobConstants.QUERY_SCORE_NOTICE_JOB_GROUP_NAME)
                .setCronExpression(DateTimeToCronUtils.getCron(noticeTaskTime,DateTimeToCronUtils.YEAR))
                .setInvokeTarget(QueryScoreNoticeJobConstants.QUERY_SCORE_NOTICE_JOB_INVOKE_TARGET);

        sysJobService.deleteJobById(queryScoreNoticeJob);
        Boolean succ2;
        try {
            succ2 = sysJobService.saveJob(queryScoreNoticeJob);
        } catch (SchedulerException e) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        if (!succ2) {
            throw new BusinessException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "发送通知失败");
        }

        LambdaUpdateWrapper<ExaminationInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ExaminationInfo::getExaminationId, examinationId)
                .set(ExaminationInfo::getResultQueryTime, noticeTaskTime);
        int updateCount = examinationInfoDao.update(null, updateWrapper);
        if (updateCount < 1) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

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
