package com.mmall.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liliang
 * @date 2017/11/20.
 */
@RequestMapping("/admin")
@Controller
public class AdminController {

    @RequestMapping("/index.page")
    public String index() {
        return "admin";
    }
}
