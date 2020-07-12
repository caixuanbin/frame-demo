package com.xbcai.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("t_qq")
public class Qq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String qqMail;
    private Date lastSendTime;
    private Integer sendCount;
    private Date createTime;
    private String qqGroup;
    private String qqGroupName;

}
