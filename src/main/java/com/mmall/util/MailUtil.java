package com.mmall.util;

import com.mmall.beans.Mail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.File;

@Slf4j
public class MailUtil {

    public static boolean send(Mail mail) {

        String from = "18107785030@163.com";
        int port = 25;
        String host = "smtp.163.com";
        String pass = "abcd1234";
        String nickname = "dubito";

        HtmlEmail email = new HtmlEmail();
        try {
            email.setHostName(host);
            email.setCharset("UTF-8");
            for (String str : mail.getReceivers()) {
                email.addTo(str);
            }
            email.setFrom(from, nickname);
            email.setSmtpPort(port);
            email.setAuthentication(from, pass);
            email.setSubject(mail.getSubject());
            String html = "<!DOCTYPE html>";
            html += "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";
            html += "<title>Insert title here</title>";
            html += "</head><body>";
            html += "<div style=\"width:600px;height:400px;margin:50px auto;\">";
            html += "<h1>权限系统为您分配的密码是<p style=\"color:red;font-size:30px;\">"+ mail.getMessage() +"</p><br/>请您妥善保管</h1><br/><br/>";
            html += "<a href=\"http://permission.dubito.cn\">点击跳转到权限系统管理页面</a>";
            html += "</div>";
            html += "</body></html>";
            email.setHtmlMsg(html);
            email.send();
            log.info("{} 发送邮件到 {}", from, StringUtils.join(mail.getReceivers(), ","));
            return true;
        } catch (EmailException e) {
            log.error(from + "发送邮件到" + StringUtils.join(mail.getReceivers(), ",") + "失败", e);
            return false;
        }
    }

}

