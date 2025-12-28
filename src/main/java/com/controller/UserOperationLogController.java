package com.controller;

import com.annotation.OperationLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.UserOperationLogEntity;
import com.entity.YonghuEntity;
import com.service.UserOperationLogService;
import com.service.impl.UserOperationLogServiceImpl;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
public class UserOperationLogController {

    @Autowired
    private UserOperationLogService userOperationLogService;

    @PostMapping("/logOperation")
    public String logOperation(@RequestParam String operationContent, @RequestParam String account, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        userOperationLogService.logOperation(operationContent, ipAddress, account);
        return "Operation logged successfully";
    }

    /**
     * 后端列表
     */
    @OperationLog(value="分页查询日志")
    @RequestMapping("/pagelog")
    public R page(@RequestParam Map<String, Object> params){
        PageUtils page = userOperationLogService.queryPage(params);
        List<UserOperationLogEntity> list= (List<UserOperationLogEntity>) page.getList();
        for(UserOperationLogEntity u:list){
            // 将 Timestamp 转换为 Instant
            Instant instant = u.getOperationTime().toInstant();
            // 将 Instant 转换为 LocalDateTime
            LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            // 创建 DateTimeFormatter 对象，指定日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 格式化输出
            String formattedTime = localDateTime.format(formatter);
            u.setTime(formattedTime);
        }
        page.setList(list);
        return R.ok().put("data", page);
    }
}    