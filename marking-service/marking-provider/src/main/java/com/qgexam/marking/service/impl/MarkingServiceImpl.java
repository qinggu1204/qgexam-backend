package com.qgexam.marking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgexam.common.core.api.AppHttpCodeEnum;
import com.qgexam.common.core.constants.ExamConstants;
import com.qgexam.common.core.exception.BusinessException;
import com.qgexam.common.core.utils.BeanCopyUtils;
import com.qgexam.common.redis.utils.RedisCache;
import com.qgexam.marking.dao.*;
import com.qgexam.marking.pojo.DTO.MarkingDTO;
import com.qgexam.marking.pojo.PO.*;
import com.qgexam.marking.pojo.VO.*;
import com.qgexam.marking.service.MarkingService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author peter guo
 * @description 教师服务实现类
 * @date 2022/12/28 20:29:50
 */
@DubboService
@Transactional
public class MarkingServiceImpl implements MarkingService {

    @Autowired
    private MarkingDao markingDao;

    @Autowired
    private ExaminationInfoDao examinationInfoDao;

    @Autowired
    private AnswerPaperInfoDao answerPaperInfoDao;

    @Autowired
    private AnswerPaperDetailDao answerPaperDetailDao;

    @Autowired
    private QuestionInfoDao questionInfoDao;

    @Autowired
    private SubQuestionAnswerDetailDao subQuestionAnswerDetailDao;

    @Autowired
    private RedisCache redisCache;

    @Override
    public IPage<TaskVO> getTaskList(Integer teacherId, Integer currentPage, Integer pageSize) {
        List<Integer> examIdList = markingDao.getExamIdList(teacherId);

        LambdaQueryWrapper<ExaminationInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(!examIdList.isEmpty(), ExaminationInfo::getExaminationId, examIdList);

        IPage<ExaminationInfo> page = new Page<>(currentPage, pageSize);
        IPage<ExaminationInfo> examinationInfos = examinationInfoDao.selectPage(page, queryWrapper);
        // 转换为视图对象
        return examinationInfos.convert(examinationInfo -> BeanCopyUtils.copyBean(examinationInfo, TaskVO.class));
    }

    @Override
    public IPage<AnswerPaperVO> getAnswerPaperList(Integer teacherId, Integer examinationId, Integer currentPage, Integer pageSize) {
        LambdaQueryWrapper<AnswerPaperInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnswerPaperInfo::getExaminationId, examinationId)
                .eq(AnswerPaperInfo::getTeacherId, teacherId);

        IPage<AnswerPaperInfo> page = new Page<>(currentPage, pageSize);
        IPage<AnswerPaperInfo> answerPaperInfos = answerPaperInfoDao.selectPage(page, queryWrapper);

        //获取第一个answerPaperId
        Integer answerPaperId = answerPaperInfos.getRecords().get(0).getAnswerPaperId();
        List<GetAnswerPaperVO> answerPaper = redisCache.getCacheList(ExamConstants.ANSWER_PAPER_KEY + answerPaperId);
        if (answerPaper.isEmpty()) {
            answerPaperInfos.getRecords().forEach(answerPaperInfo -> {
                List<GetAnswerPaperVO> answerPaperList = getAnswerPaper(answerPaperInfo.getAnswerPaperId());
                redisCache.setCacheList(ExamConstants.ANSWER_PAPER_KEY + answerPaperInfo.getAnswerPaperId(), answerPaperList);
            });
        }

        // 转换为视图对象
        return answerPaperInfos.convert(answerPaperInfo -> BeanCopyUtils.copyBean(answerPaperInfo, AnswerPaperVO.class));
    }

    @Override
    public List<GetAnswerPaperVO> getAnswerPaper(Integer answerPaperId) {
        //从redis中获取答卷
        List<GetAnswerPaperVO> answerPaper = redisCache.getCacheList(ExamConstants.ANSWER_PAPER_KEY + answerPaperId);
        if (!answerPaper.isEmpty()) {
            return answerPaper;
        }

        //根据答卷id查询考试编号
        Integer examinationId = answerPaperInfoDao.selectById(answerPaperId).getExaminationId();

        //查询该考试的试卷编号
        Integer examinationPaperId = examinationInfoDao.selectById(examinationId).getExaminationPaperId();

        //查询该考试的主观题编号
        List<Integer> subQuestionIdList = examinationInfoDao.selectSubQuestionIdList(examinationPaperId);
        //若没有主观题则不需用到教师阅卷，直接抛出异常
        if (subQuestionIdList.isEmpty()) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //根据主观题编号查询试题编号和分数
        List<GetAnswerPaperVO> getAnswerPaperVOList = examinationInfoDao.selectQuestionList(subQuestionIdList);

        //将答卷信息转换为map
        Map<Integer,GetAnswerPaperVO> getAnswerPaperVOMap = getAnswerPaperVOList.stream()
                .collect(Collectors.toMap(GetAnswerPaperVO::getQuestionId, getAnswerPaperVO -> getAnswerPaperVO));

        //获取试题编号
        List<Integer> questionIdList = getAnswerPaperVOList.stream()
                .map(GetAnswerPaperVO::getQuestionId)
                .collect(Collectors.toList());

        if (questionIdList.isEmpty()) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //获取没有小题的试题编号
        LambdaQueryWrapper<QuestionInfo> noSubQuestionQueryWrapper = new LambdaQueryWrapper<>();
        noSubQuestionQueryWrapper.in(QuestionInfo::getQuestionId, questionIdList)
                .eq(QuestionInfo::getHasSubQuestion, 0);
        List<Integer> noSubQuestionIdList = questionInfoDao.selectList(noSubQuestionQueryWrapper).stream()
                .map(QuestionInfo::getQuestionId)
                .collect(Collectors.toList());

        //获取有小题的试题编号
        List<Integer> hasSubQuestionIdList = questionIdList;
        hasSubQuestionIdList = hasSubQuestionIdList.stream()
                .filter(questionId -> !noSubQuestionIdList.contains(questionId))
                .collect(Collectors.toList());

        //查询所有题目的题干和答案信息
        LambdaQueryWrapper<QuestionInfo> questionInfoQueryWrapper = new LambdaQueryWrapper<>();
        questionInfoQueryWrapper.in(QuestionInfo::getQuestionId, questionIdList);
        List<QuestionInfo> questionInfoList = questionInfoDao.selectList(questionInfoQueryWrapper);
        List<QuestionAnsVO> questionAnsVOList = BeanCopyUtils.copyBeanList(questionInfoList, QuestionAnsVO.class);

        //将所有题目的答案信息添加到题目信息中
        questionAnsVOList.forEach(questionAnsVO -> {
            getAnswerPaperVOMap.get(questionAnsVO.getQuestionId()).setDescription(questionAnsVO.getDescription());
            getAnswerPaperVOMap.get(questionAnsVO.getQuestionId()).setQuestionAns(questionAnsVO.getQuestionAns());
            getAnswerPaperVOMap.get(questionAnsVO.getQuestionId()).setHasSubQuestion(questionAnsVO.getHasSubQuestion());
        });

        //查询有小题的题目答案信息
        if (!hasSubQuestionIdList.isEmpty()) {
            List<QuestionAnsVO> hasSubQuestionAnsVOList = questionInfoDao.getHasSubQuestionAnsList(hasSubQuestionIdList);

            //将有小题题目的答案信息添加到题目信息中
            hasSubQuestionAnsVOList.forEach(hasSubQuestionAnsVO -> {
                getAnswerPaperVOMap.get(hasSubQuestionAnsVO.getQuestionId())
                        .setSubQuestionInfo(hasSubQuestionAnsVO.getSubQuestionAnsList());
            });
        }

        //查询学生所有题目答案
        LambdaQueryWrapper<AnswerPaperDetail> answerPaperDetailQueryWrapper = new LambdaQueryWrapper<>();
        answerPaperDetailQueryWrapper.eq(AnswerPaperDetail::getAnswerPaperId, answerPaperId)
                .in(AnswerPaperDetail::getQuestionId, questionIdList);
        List<AnswerPaperDetail> answerPaperDetailList = answerPaperDetailDao.selectList(answerPaperDetailQueryWrapper);

        //将学生题目答案和教师打分添加到题目信息中
        answerPaperDetailList.forEach(answerPaperDetail -> {
            getAnswerPaperVOMap.get(answerPaperDetail.getQuestionId()).setStudentAnswer(answerPaperDetail.getStudentAnswer());
            getAnswerPaperVOMap.get(answerPaperDetail.getQuestionId()).setScore(answerPaperDetail.getScore());
        });

        //获取有小题题目的answerPaperDetailId
        if (!hasSubQuestionIdList.isEmpty()) {
            answerPaperDetailQueryWrapper.select(AnswerPaperDetail::getAnswerPaperDetailId)
                    .eq(AnswerPaperDetail::getAnswerPaperId, answerPaperId)
                    .in(AnswerPaperDetail::getQuestionId, hasSubQuestionIdList);
            List<Integer> answerPaperDetailIdList = answerPaperDetailDao.selectList(answerPaperDetailQueryWrapper).stream()
                    .map(AnswerPaperDetail::getAnswerPaperDetailId)
                    .collect(Collectors.toList());

            //查询学生有小题题目答案
            LambdaQueryWrapper<SubQuestionAnswerDetail> subQuestionAnswerQueryWrapper = new LambdaQueryWrapper<>();
            subQuestionAnswerQueryWrapper.in(SubQuestionAnswerDetail::getAnswerPaperDetailId, answerPaperDetailIdList);
            List<SubQuestionAnswerDetail> hasSubQuestionAnswerList = subQuestionAnswerDetailDao.selectList(subQuestionAnswerQueryWrapper);

            //将有小题答案转换为map
            Map<Integer, String> hasSubQuestionAnswerMap = hasSubQuestionAnswerList.stream()
                    .collect(Collectors.toMap(SubQuestionAnswerDetail::getSubQuestionId, SubQuestionAnswerDetail::getSubQuestionAnswer));

            //将学生题目答案添加到题目信息中
            getAnswerPaperVOMap.forEach((questionId, getAnswerPaperVO) -> {
                if (getAnswerPaperVO.getHasSubQuestion() == 1) {
                    getAnswerPaperVO.getSubQuestionInfo().forEach(subQuestionAnsVO -> {
                        subQuestionAnsVO.setSubQuestionAnswer(hasSubQuestionAnswerMap.get(subQuestionAnsVO.getSubQuestionId()));
                    });
                }
            });
        }

        //将map转换为list
        getAnswerPaperVOList = new ArrayList<>(getAnswerPaperVOMap.values());

        return getAnswerPaperVOList;
    }

    @Override
    public void marking(Integer answerPaperId, List<MarkingDTO> questionList) {
        //批量更新数据库学生得分
        answerPaperDetailDao.updateScoreBatch(answerPaperId, questionList);
        //从数据库中查询答卷更新redis中的答卷信息
        redisCache.deleteObject(ExamConstants.ANSWER_PAPER_KEY + answerPaperId);
        List<GetAnswerPaperVO> answerPaper = getAnswerPaper(answerPaperId);
        redisCache.setCacheList(ExamConstants.ANSWER_PAPER_KEY + answerPaperId, answerPaper);

        //算出学生的主观题总得分
        List<Integer> scoreList = questionList.stream()
                .map(MarkingDTO::getScore)
                .collect(Collectors.toList());
        Integer subScore = scoreList.stream().reduce(0, Integer::sum);
        //将学生的主观题得分更新到答卷表中
        LambdaUpdateWrapper<AnswerPaperInfo> answerPaperUpdateWrapper = new LambdaUpdateWrapper<>();
        answerPaperUpdateWrapper.set(AnswerPaperInfo::getSubjectiveScore, subScore)
                .eq(AnswerPaperInfo::getAnswerPaperId, answerPaperId);
        int count = answerPaperInfoDao.update(null, answerPaperUpdateWrapper);
        if (count < 1) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //从答卷表里查询出学生的客观题得分
        Integer objScore = answerPaperInfoDao.selectById(answerPaperId).getObjectiveScore();

        //将学生的客观题得分和主观题得分相加得到学生的总得分
        Integer totalScore = objScore + subScore;
        answerPaperUpdateWrapper.set(AnswerPaperInfo::getPaperTotalScore, totalScore)
                .set(AnswerPaperInfo::getIsMarked, 1)
                .eq(AnswerPaperInfo::getAnswerPaperId, answerPaperId);
        count = answerPaperInfoDao.update(null, answerPaperUpdateWrapper);
        if (count < 1) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //根据答卷id查询出学生的id和考试id
        Integer studentId = answerPaperInfoDao.selectById(answerPaperId).getStudentId();
        Integer examinationId = answerPaperInfoDao.selectById(answerPaperId).getExaminationId();

        //根据考试id查询课程id
        List<Integer> courseIdList = examinationInfoDao.selectCourseIdList(examinationId);
        if (courseIdList.isEmpty()) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //根据学生id和课程id查询学生选课，将成绩插入课程成绩
        Integer updateCount = examinationInfoDao.updateCourseScore(studentId, courseIdList, totalScore);
        if (updateCount < 1) {
            throw BusinessException.newInstance(AppHttpCodeEnum.SYSTEM_ERROR);
        }

    }


}
