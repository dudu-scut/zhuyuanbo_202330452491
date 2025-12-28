package com.aspect;


import com.annotation.OperationLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.ShangjiaEntity;
import com.entity.YonghuEntity;
import com.entity.view.ShangjiaView;
import com.entity.vo.YonghuVO;
import com.service.ShangjiaService;
import com.service.YonghuService;
import com.service.impl.UserOperationLogServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class OperationLogAspect {
    @Autowired
    private YonghuService yonghuService;
    @Autowired
    private ShangjiaService shangjiaService;
    @Autowired
    private UserOperationLogServiceImpl userOperationLogServiceImpl;

    /**
     * @description: aop 切面
     **/
    @After("@annotation(com.annotation.OperationLog)")
    public void logOperation(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String ipAddress = request.getRemoteAddr();
            String requestUrl = request.getRequestURL().toString();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperationLog operationLogAnnotation = method.getAnnotation(OperationLog.class);
            String operationContent = operationLogAnnotation.value();
            // 检查是否为 login 请求
            if (requestUrl.contains("/session")) {
                return;
            }
            String account = "";
            if (requestUrl.contains("/login")) {
                String username = request.getParameter("username");
                if (username != null) {
                    account = username;
                }
            } else {
                String adminName = request.getHeader("AdminName");
                String userId = request.getHeader("userid");
                if (userId != null && adminName == null) {
                    Wrapper<YonghuEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("id", userId);
                    YonghuEntity y = yonghuService.selectView(wrapper);
                    Wrapper<ShangjiaEntity> w = new EntityWrapper<>();
                    w.eq("id", userId);
                    ShangjiaView sj = shangjiaService.selectView(w);
                    if (y == null) {
                        account = sj.getZhanghao();
                    } else {
                        account = y.getZhanghao();
                    }
                } else if (userId == null && adminName != null)
                    account = adminName;
            }
            userOperationLogServiceImpl.logOperation(operationContent, ipAddress, account);
        }
    }
}    