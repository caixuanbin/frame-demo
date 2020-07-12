package com.xbcai.service.mail;

import javax.mail.MessagingException;

public interface MailService {
    void sendInlineResourceMail(String to, String subject, String content, String rscPath,String h,String w) throws MessagingException;

    String sendMail(String qqGroup,String sex);
}
