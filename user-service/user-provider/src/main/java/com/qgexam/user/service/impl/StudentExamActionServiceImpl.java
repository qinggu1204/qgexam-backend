package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.StudentExamActionDao;
import com.qgexam.user.pojo.PO.StudentExamAction;
import com.qgexam.user.service.StudentExamActionService;
import org.apache.dubbo.config.annotation.DubboService;
/**
 * (StudentExamAction)表服务实现类
 *
 * @author peter guo
 * @since 2023-01-18 12:28:39
 */
@DubboService
public class StudentExamActionServiceImpl extends ServiceImpl<StudentExamActionDao, StudentExamAction> implements StudentExamActionService {

}

