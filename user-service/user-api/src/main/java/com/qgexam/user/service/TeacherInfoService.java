package com.qgexam.user.service;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qgexam.user.pojo.PO.TeacherInfo;
import com.qgexam.user.pojo.VO.GetTeacherInfoVO;

/**
 * 教师表(TeacherInfo)表服务接口
 *
 * @author tageshi
 * @since 2022-12-16 17:28:21
 */
public interface TeacherInfoService extends IService<TeacherInfo> {
    GetTeacherInfoVO getTeacherInfo(SaSession session);

}

