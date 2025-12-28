package com.service;


import com.baomidou.mybatisplus.service.IService;
import com.entity.UserOperationLogEntity;
import com.utils.PageUtils;

import java.util.Map;

public interface UserOperationLogService extends IService<UserOperationLogEntity> {
    void logOperation(String operationContent, String ipAddress, String account);
    PageUtils queryPage(Map<String, Object> params);
}