package com.qgexam.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgexam.user.dao.RoleInfoDao;
import com.qgexam.user.pojo.PO.RoleInfo;
import com.qgexam.user.service.RoleInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;
/**
 * 角色表(RoleInfo)表服务实现类
 *
 * @author lamb007
 * @since 2022-12-11 21:18:02
 */
@DubboService
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoDao, RoleInfo> implements RoleInfoService {

}

