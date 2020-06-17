package com.xbcai.exception;

import com.xbcai.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;


@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value =  MessagingException.class)
    public Result messagingExceptionHandler(MessagingException ex){
        return Result.fail(500,"邮件发送异常："+ex.getMessage(),ex);
    }
    /**
     * 全局异常
     * @param ex 异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception ex) {
        return Result.fail(500,ex.getMessage(),ex);
    }
}
