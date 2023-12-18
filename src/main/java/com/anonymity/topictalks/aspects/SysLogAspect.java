package com.anonymity.topictalks.aspects;

import com.alibaba.fastjson.JSON;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.aspects
 * - Created At: 15-09-2023 21:57:05
 * @since 1.0 - version of class
 */

@Slf4j
@Aspect
@Component
public class SysLogAspect {

    @Pointcut("execution(* com.anonymity.topictalks.controllers..*.*(..))")
    public void log() {}

    @Before("log()")
    public void  doBefore(JoinPoint joinPoint) throws UnsupportedEncodingException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String method = request.getMethod();
        StringBuffer paramsValue = new StringBuffer();
        Object paramsName=null;
        // get request
        if (HttpMethod.GET.toString().equals(method)) {
            String queryString = request.getQueryString();
            if (!StringUtils.isEmpty(queryString)) {
                paramsName= JSON.parseObject(JSON.toJSONString(joinPoint.getSignature())).get("parameterNames");
                paramsValue.append( URLDecoder.decode(queryString,"UTF-8"));
            }
        } else {
            // other request
            Object[] paramsArray = joinPoint.getArgs();
            paramsName= JSON.parseObject(JSON.toJSONString(joinPoint.getSignature())).get("parameterNames");
            for (Object o :paramsArray){
                paramsValue.append(o+" ");
            }
        }
        log.info("URLParamName  : " + paramsName);
        log.info("URLParamValue  : " + paramsValue);
        log.info("URL:  {}, HTTP_METHOD:  {}, IP:  {}, Method:  {} ",request.getRequestURL().toString(),request.getMethod(), "."+joinPoint.getSignature().getName());
    }

}
