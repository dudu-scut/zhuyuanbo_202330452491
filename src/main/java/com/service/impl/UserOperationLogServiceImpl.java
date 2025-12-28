package com.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.UserOperationLogDao;
import com.entity.UserOperationLogEntity;
import com.entity.YonghuEntity;
import com.entity.view.YonghuView;
import com.service.UserOperationLogService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserOperationLogServiceImpl  extends ServiceImpl<UserOperationLogDao, UserOperationLogEntity> implements UserOperationLogService{

    @Autowired
    private UserOperationLogDao userOperationLogDao;

    public void logOperation(String operationContent, String ipAddress, String account) {
        userOperationLogDao.insertOperationLog(operationContent, ipAddress, account);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<UserOperationLogEntity> page =new Query<UserOperationLogEntity>(params).getPage();
        page.setRecords(baseMapper.selectListView(page));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }
}    