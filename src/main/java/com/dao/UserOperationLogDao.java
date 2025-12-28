package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.entity.UserOperationLogEntity;
import com.entity.YonghuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserOperationLogDao  extends BaseMapper<UserOperationLogEntity> {

    void insertOperationLog(String operationContent, String ipAddress, String account);

    List<UserOperationLogEntity> selectListView(Page<UserOperationLogEntity> page);
}