package com.mmall.common;

import com.mmall.exception.ParamException;
import com.mmall.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 监听类
 * @author liliang
 * @date 2017/11/16.
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    private static final String JSON_VIEW = "jsonView";
    private static final String EXCEPTION = "exception";

    /**
     * 要求项目所有请求 json 数据,都使用.json 结尾
     * 要求项目所有请求  page 页面,都使用.page 结尾
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "系统异常,请检查输入的格式是否正确";
        JsonData fail = JsonData.fail(defaultMsg);
        if (url.endsWith(".json")) {
            if (ex instanceof PermissionException || ex instanceof ParamException) {
                JsonData result = JsonData.fail(ex.getMessage());
                mv = new ModelAndView(JSON_VIEW, result.toMap());
            } else {
                log.error("未知 json 异常.url:" + url, ex);
                JsonData result = fail;
                mv = new ModelAndView(JSON_VIEW, result.toMap());
            }
        } else if (url.endsWith(".page")) {
            log.error("未知 page 异常.url:" + url, ex);
            JsonData result = fail;
            mv = new ModelAndView(EXCEPTION, result.toMap());
        } else {
            log.error("未知异常.url:" + url, ex);
            JsonData result = fail;
            mv = new ModelAndView(JSON_VIEW, result.toMap());
        }
        return mv;
    }
}
