package com.xbcai.mail;

import com.xbcai.date.JdkDate;
import com.xbcai.model.Qq;
import com.xbcai.result.Result;
import com.xbcai.service.mail.MailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class MailCtrl {
    @Autowired
    private MailService mailService;

    @PostMapping("/sendInlineResourceMail")
    public Result<String> sendInlineResourceMail(@RequestBody Map<String,String> maps) throws MessagingException {
        String to = maps.get("to");
        String subject = maps.get("subject");
        String content = maps.get("content");
        String rscPath = maps.get("resPath");
        mailService.sendInlineResourceMail(to,subject,content,rscPath,null,null);
        return Result.success("发送成功");
    }

    @PostMapping("/sendMail")
    public Result<String> sendMail(String qqGroup,String sex){
       return Result.success(mailService.sendMail(qqGroup,sex));
    }

}
