package com.jy.common.annotation;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jy.common.utils.IPUtil;
import com.jy.common.utils.security.AccountShiroUtil;
import com.jy.entity.system.account.Account;
import com.jy.service.system.log.OptLogService;

@Aspect
@Component
@Order(1)// Order值越小优先被加载
public class OperateAspect {

	private final Log logger = LogFactory.getLog(OperateAspect.class);

	private Boolean recordOptLog = true;

	@Autowired
	OptLogService optLogService;

	@Around(value = "@annotation(com.jy.common.annotation.ControllerOptLog)")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		if (recordOptLog) {
			try {
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
						.getRequest();
				String ip = IPUtil.getIpAddr(request);
				MethodSignature signature = (MethodSignature) joinPoint.getSignature();
				Method method = signature.getMethod();
				Account curUser = AccountShiroUtil.getCurrentUser();
				String userId = "";
				if (curUser != null) {
					userId = curUser.getAccountId();
				}
				ControllerOptLog controllerOptLog = method.getAnnotation(ControllerOptLog.class);
				String desc = controllerOptLog.desc();
				RequestMapping requestMappingAnnotation = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
				String url = "";
				if (requestMappingAnnotation != null) {
					String[] temp = requestMappingAnnotation.value();
					url = StringUtils.join(temp);
				}
				optLogService.log(desc, method.toString(), url, ip, userId, args);
			} catch (Throwable e) {
				logger.error("记录操作日志错误", e);
			}
		}
		Object rt = joinPoint.proceed(args);
		return rt;
	}

}
