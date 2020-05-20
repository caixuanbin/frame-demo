package com.xbcai.ctrl.exception;

import com.xbcai.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalException {
    /**
     * 全局异常
     * @param ex 异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception ex) {
        return Result.fail(500,ex.getMessage(),ex);
    }
}
