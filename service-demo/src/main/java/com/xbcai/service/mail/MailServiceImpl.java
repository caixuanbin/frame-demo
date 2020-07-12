package com.xbcai.service.mail;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xbcai.dao.QqMapper;
import com.xbcai.date.JdkDate;
import com.xbcai.mail.MailUtils;
import com.xbcai.mail.TaoBao;
import com.xbcai.mail.TaoBaoProductUtils;
import com.xbcai.model.Qq;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MailServiceImpl  implements MailService {
    @Autowired
    private MailUtils mailUtils;
    @Autowired
    private QqMapper qqMapper;
    @Override
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath,String h,String w) throws MessagingException {
        mailUtils.sendInlineResourceMail(to,subject,content,rscPath,h,w);
    }

    @Override
    public String sendMail(String qqGroup,String sex) {
        TaoBao tb = TaoBaoProductUtils.heibao();
        List<Qq> allQqs = qqMapper.findAllQqs(qqGroup,sex);
        int size = 20;
        StringBuilder sb = new StringBuilder();
        int j = 0;
        int sendCount = 0;
        List<Integer> ids = new ArrayList<>();
        for(int i=0;i<allQqs.size();i++){
            Qq qq = allQqs.get(i);
            //今天已经发过的不再发送
            if(qq.getLastSendTime()!=null&& StringUtils.equals(JdkDate.format(qq.getLastSendTime(),"yyyyMMdd"),
                    JdkDate.format(new Date(),"yyyyMMdd"))){continue;}
            sendCount++;
            if(sendCount>200) {break;}
            try{
                if(j==size||(j<size&&i==allQqs.size()-1)){
                    this.sendInlineResourceMail(sb.toString(), tb.getSubject(), tb.getContent(), tb.getImgPath(),tb.getH(),tb.getH());
                    Thread.sleep(3000);
                    System.out.println("成功发送："+sb.toString());
                    sb.setLength(0);
                    //sb.append("461435481@qq.com;");
                    j=0;
                    ids.forEach(item->qqMapper.updateSendInfo(item));
                    ids.clear();
                }
                sb.append(qq.getQqMail()).append(";");
                ids.add(qq.getId());
                j++;
            }catch (Exception e){
                System.out.println("发送失败，邮箱："+sb.toString()+"\n,error:"+e.getMessage());
                return sb.toString();
            }finally {
                ids.forEach(item->qqMapper.updateSendInfo(item));
                System.out.println("更新已发送记录："+ids);
                ids.clear();
            }
        }
        System.out.println("=====================总共发送邮件数：================================"+sendCount);
        return "ok";
    }

}
