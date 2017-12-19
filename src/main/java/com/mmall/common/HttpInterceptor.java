package com.mmall.common;

import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * HTTP 请求前后监听器工具类
 * @author liliang
 * @date 2017/11/16.
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final String START_TIME = "startTime";

    /**
     * 请求处理之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        Map parameterMap = request.getParameterMap();
        log.info("请求成功之前   url:{},param:{}",url, JsonMapper.objToString(parameterMap));
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME,startTime);
        return  true;
    }

    /**
     * 请求处理完成之后(请求成功之后执行)
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String url = request.getRequestURL().toString();
//        long startTime = (long)request.getAttribute(START_TIME);
//        long endTime = System.currentTimeMillis();
//        log.info("请求成功之后  url:{}, cost:{}",url, endTime - startTime);
        removeThreadLocalInfo();
    }

    /**
     * 请求处理结束之后执行(任何时候都会执行)
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        long startTime = (long)request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        log.info("请求成功或失败之后   url:{},cost:{}",url, endTime - startTime);
        removeThreadLocalInfo();
    }

    public void removeThreadLocalInfo() {
        RequestHolder.remove();
    }
}
