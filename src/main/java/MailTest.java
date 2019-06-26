import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import com.sun.mail.util.MailSSLSocketFactory;


public class MailTest {

    public static void main(String[] args) throws Exception {
        //建立一个空属性列表
        Properties props = new Properties(); //该类主要用于读取Java的配置文件，是以键值对的形式进行参数配置的。
        // 开启debug调试
        props.setProperty("mail.debug", "true");//调用的是hashtable的put方法
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        // 根据propertise创建会话
        Session session = Session.getInstance(props);
        //非单例模式，单例模式可以调用getDefaultInstance(props),因为username和password都是final的

        // 根据会话创建邮件信息
        Message msg = new MimeMessage(session);
        // 邮件主题
        msg.setSubject("JavaMail —— 2333 —— Test");

        //创建图片节点
        MimeBodyPart image = new MimeBodyPart();
        //读取本地文件
        DataHandler dataHandler = new DataHandler(new FileDataSource("D:\\QQ\\1916417519\\FileRecv\\MobileFile\\2333.jpg"));
        //将图片添加至结点
        image.setDataHandler(dataHandler);
        //为"节点"设置一个唯一编号
        image.setContentID("picture");


        // 创建文本"节点"
        MimeBodyPart text = new MimeBodyPart();
        // 这里添加图片的方式是将整个图片包含到邮件内容中
        text.setContent("<br/><h2>mail_mail_mail<h2>"
                + "<a href='https://me.csdn.net/lr1916417519'>网址链接测试</a>"
                + "<img src='cid:pic'/>", "text/html;charset=UTF-8");


        // 创建附件结点(源代码)
        MimeBodyPart attachment  = new MimeBodyPart();
        // 读取本地文件
        DataHandler dataHandler1 = new DataHandler(new FileDataSource("src/main/java/MailTest.java"));
        // 将文件添加至结点
        attachment.setDataHandler(dataHandler1);
        // 设置附件的文件名（需要编码）
        attachment.setFileName(MimeUtility.encodeText(dataHandler1.getName()));


        // 创建附件结点(mail.jar包)
        MimeBodyPart jar  = new MimeBodyPart();
        // 读取本地文件
        DataHandler dataHandler2 = new DataHandler(new FileDataSource("src/lib/mail-1.5.0-b01.jar"));
        // 将文件添加至结点
        jar.setDataHandler(dataHandler2);
        // 设置附件的文件名（需要编码）
        jar.setFileName(MimeUtility.encodeText(dataHandler2.getName()));


        // 创建混合节点  将图片节点 文件结点 附件结点 加入
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(image);
        multipart.addBodyPart(text);
        multipart.addBodyPart(attachment);
        multipart.addBodyPart(jar);
        // 将混合节点加入邮件中
        msg.setContent(multipart);

        // 设置邮件发送方
        msg.setFrom(new InternetAddress("1916417519@qq.com"));//发送方的邮箱地址

        // 开始会话传输
        Transport transport = session.getTransport();
        // 连接邮箱   加入自己（发送方）授权码      
        transport.connect("smtp.qq.com", "1916417519@qq.com", "mdewzivktpkydjac");//发送方邮箱和授权码

        // 给目标邮箱发送邮件
        transport.sendMessage(msg, new Address[] { new InternetAddress("xiamuzi0424@163.com") });//接收方的邮箱地址
        transport.close();
    }
}
