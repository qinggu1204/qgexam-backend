package com.qgexam.user.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.constants.JobConstants;
import com.qgexam.common.core.constants.MessageConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.core.utils.DateTimeToCronUtils;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.user.dao.*;
import com.qgexam.user.pojo.DTO.CreatePaperDTO;
import com.qgexam.user.pojo.DTO.QuestionDTO;
import com.qgexam.user.pojo.PO.*;
import com.qgexam.user.pojo.VO.ChapterImportanceLevelVO;
import com.qgexam.user.pojo.VO.SubjectVO;
import com.qgexam.user.service.NeTeacherInfoService;
import com.qgexam.common.core.utils.QuartzManager;
import com.qgexam.user.task.BeginCacheTask;
import com.qgexam.user.task.QueryScoreNoticeTask;
import org.apache.dubbo.config.annotation.DubboService;
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
@Transactional
public class NeTeacherInfoServiceImpl implements NeTeacherInfoService {
    @Autowired
    private TeacherInfoDao teacherInfoDao;

    @Autowired
    private ExaminationPaperDao examinationPaperDao;

    @Autowired
    private ChapterInfoDao chapterInfoDao;

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
    private StudentInfoDao studentInfoDao;

    @Autowired
    private RedisCache redisCache;


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
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

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
            messageInfo.setEndTime(endTime);
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
            messageInfo.setStartTime(endTime);
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
        //将答卷编号列表存入redis中
        redisCache.setCacheList(ExamConstants.ANSWER_PAPER_ID_LIST_KEY, answerPaperIdList);

        //定时任务，通知答卷可以开始缓存
        QuartzManager.addJob(JobConstants.BEGIN_CACHE_JOB_NAME, JobConstants.JOB_GROUP_NAME,
                JobConstants.BEGIN_CACHE_TRIGGER_NAME, JobConstants.TRIGGER_GROUP_NAME,
                BeginCacheTask.class, DateTimeToCronUtils.getCron(endTime,DateTimeToCronUtils.YEAR));

        //延迟一小时发送将学生查询成绩通知置为可查询到
        LocalDateTime noticeTaskTime = LocalDateTimeUtil.offset(LocalDateTimeUtil.of(endTime), 1, ChronoUnit.HOURS);

        //定时任务，定时将学生查询成绩通知逻辑删除置为0即可以查询到
        QuartzManager.addJob(JobConstants.QUERY_SCORE_NOTICE_JOB_NAME, JobConstants.JOB_GROUP_NAME,
                JobConstants.QUERY_SCORE_NOTICE_TRIGGER_NAME, JobConstants.TRIGGER_GROUP_NAME,
                QueryScoreNoticeTask.class, DateTimeToCronUtils.getCron(noticeTaskTime,DateTimeToCronUtils.YEAR));
    }

}
