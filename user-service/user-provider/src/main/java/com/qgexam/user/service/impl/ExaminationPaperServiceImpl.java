package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.ExaminationPaperDao;
import com.qgexam.user.pojo.PO.ExaminationPaper;
import com.qgexam.user.service.ExaminationPaperService;
import org.apache.dubbo.config.annotation.DubboService;
/**
 * 试卷信息表(ExaminationPaper)表服务实现类
 *
 * @author lamb007
 * @since 2023-01-09 11:38:12
 */
@DubboService
public class ExaminationPaperServiceImpl extends ServiceImpl<ExaminationPaperDao, ExaminationPaper> implements ExaminationPaperService {

}

