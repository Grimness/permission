import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author liliang
 * @date 2017/12/11.
 */
public class MailUtils {

    /**
     * 发送邮件
     * @param toMail
     * @param password
     */
    public static void sendMail(String toMail, String password) throws Exception {

        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.163.com");
        //props.setProperty("mail.port", "25");

        Session session = Session.getInstance(props,
                new Authenticator() {	//����ģʽ
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication("18107785030@163.com", "abcd1234");
                    }
                });
        session.setDebug(true);

        Message msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress("18107785030@163.com"));
            msg.setSubject("JavaMail API");
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toMail));
            msg.setContent(password,"text/html;charset=GBK");

            Transport.send(msg);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        // 创建连接邮箱服务器
//        Session session = Session.getInstance(props, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("18107785030@163.com","ABCde12345");
//            }
//        });
//
//        // 创建邮件对象
//        MimeMessage message = new MimeMessage(session);
//        // 设置发件人
//        message.setFrom(new InternetAddress("18107785030@163.com"));
//        // 设置收件人
//        message.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(toMail));
//        // 设置邮件主题
//        message.setSubject("密码分配通知");
//        // 设置正文
//        message.setContent(password,"UTF-8");
//        // 发送邮件
//        Transport.send(message);
    }

    public static void main(String[] args) {
        try {
            sendMail("809005117@qq.com","123456");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
