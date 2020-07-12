package com.xbcai.mail;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class MailUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.fromMail.addr}")
    private String from;

    /**
     * 发送文本邮件
     * @param to
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String to, String subject, String content)  {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        logger.info("简单邮件已经发送。");
    }

    /**
     * 发送html邮件
     * @param to
     * @param subject
     * @param content
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = assembleMessage(to,subject,content,null,null,null,null);
        mailSender.send(message);
        logger.info("html邮件发送成功");
    }


    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws  MessagingException{
        MimeMessage message = assembleMessage(to,subject,content,null,filePath,null,null);
        mailSender.send(message);
        logger.info("带附件的邮件已经发送。");
    }


    /**
     * 发送正文中有静态资源（图片）的邮件
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     */
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath,String h,String w) throws  MessagingException{
        MimeMessage message = assembleMessage(to,subject,content,rscPath,null,h,w);
        mailSender.send(message);
        logger.info("嵌入静态资源的邮件已经发送，发送邮箱："+to);
    }

    /**
     * 组装邮件
     * @param to 目的地
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param inlineResourcesPath 内嵌资源路径例如图片路径
     * @param attachmentPath 邮件附件路径
     * @param h 内嵌图片高度
     * @param w 内嵌图片宽度
     * @return 返回组装好的邮件
     * @throws MessagingException
     */
    MimeMessage assembleMessage(String to,String subject,String content,String inlineResourcesPath,String attachmentPath,String h,String w) throws MessagingException{
        String height="1141";
        String width="828";
        if(!StringUtils.isEmpty(h)){
            height = h;
        }
        if(StringUtils.isEmpty(w)){
            width = w;
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to.split(";"));
        helper.setSubject(subject);
        if(!StringUtils.isEmpty(inlineResourcesPath)){
            String rscId = System.currentTimeMillis()+"";
            //content="<html><body>"+content+"<br/><img src=\'cid:" + rscId + "\' height=\"614\" width=\"350\"></body></html>";
            content="<html><body>"+content+"<br/><img src=\'cid:" + rscId + "\' height=\""+height+"\" width=\""+width+"\"></body></html>";
            helper.setText(content, true);
            FileSystemResource res = new FileSystemResource(new File(inlineResourcesPath));
            helper.addInline(rscId, res);
        }
        if(!StringUtils.isEmpty(attachmentPath)){
            helper.setText(content, true);
            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            String fileName = attachmentPath.substring(attachmentPath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
        }
        return  message;
    }

    /**
     * 验证邮箱是否存在
     * <br>
     * 由于要读取IO，会造成线程阻塞
     *
     * @param toMail
     *         要验证的邮箱
     * @param domain
     *         发出验证请求的域名(是当前站点的域名，可以任意指定)
     * @return
     *         邮箱是否可达
     */
    public static boolean valid(String toMail, String domain) {
        if(StringUtils.isBlank(toMail) || StringUtils.isBlank(domain)) {return false;}
        if(!StringUtils.contains(toMail, '@')) {return false;}
        String host = toMail.substring(toMail.indexOf('@') + 1);
        //if(host.equals(domain)) {return false;}
        Socket socket = new Socket();
        try {
            // 查找mx记录
            Record[] mxRecords = new Lookup(host, Type.MX).run();
            if(ArrayUtils.isEmpty(mxRecords)) {return false;}
            // 邮件服务器地址
            String mxHost = ((MXRecord)mxRecords[0]).getTarget().toString();
            // 优先级排序
            if(mxRecords.length > 1) {
                List<Record> arrRecords = new ArrayList<Record>();
                Collections.addAll(arrRecords, mxRecords);
                Collections.sort(arrRecords,(Record o1, Record o2)->new CompareToBuilder().append(((MXRecord)o1).getPriority(), ((MXRecord)o2).getPriority()).toComparison());
                mxHost = ((MXRecord)arrRecords.get(0)).getTarget().toString();
            }
            // 开始smtp
            socket.connect(new InetSocketAddress(mxHost, 25));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // 超时时间(毫秒)
            long timeout = 6000;
            // 睡眠时间片段(50毫秒)
            int sleepSect = 50;

            // 连接(服务器是否就绪)
            if(getResponseCode(timeout, sleepSect, bufferedReader) != 220) {
                return false;
            }

            // 握手
            bufferedWriter.write("HELO " + domain + "\r\n");
            bufferedWriter.flush();
            if(getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 身份
            bufferedWriter.write("MAIL FROM: <check@" + domain + ">\r\n");
            bufferedWriter.flush();
            if(getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 验证
            bufferedWriter.write("RCPT TO: <" + toMail + ">\r\n");
            bufferedWriter.flush();
            if(getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 断开
            bufferedWriter.write("QUIT\r\n");
            bufferedWriter.flush();
            return true;
        } catch (NumberFormatException e) {
        } catch (IOException e) {
        } catch (InterruptedException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        return false;
    }


    private static int getResponseCode(long timeout, int sleepSect, BufferedReader bufferedReader) throws InterruptedException, NumberFormatException, IOException {
        int code = 0;
        for(long i = sleepSect; i < timeout; i += sleepSect) {
            Thread.sleep(sleepSect);
            if(bufferedReader.ready()) {
                String outline = bufferedReader.readLine();
                // FIXME 读完……
                while(bufferedReader.ready()) {
                    /*System.out.println(*/
                    bufferedReader.readLine()/*)*/;
                    /*System.out.println(outline);*/
                    code = Integer.parseInt(outline.substring(0, 3));
                    break;
                }
            }
        }
        return code;
    }

    public static void main(String[] args) {
        System.out.println(valid("461435481@qq.com","qq.com"));
    }
}
