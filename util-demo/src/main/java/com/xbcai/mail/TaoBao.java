package com.xbcai.mail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaoBao{
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 邮件内嵌图片
     */
    private String imgPath;
    /**
     * 宽度
     */
    private String w;
    /**
     * 高度
     */
    private String h;
}