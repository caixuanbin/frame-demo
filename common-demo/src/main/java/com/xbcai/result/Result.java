package com.xbcai.result;

import com.xbcai.enumeration.ResultEnum;
import lombok.Data;

/**
 * 控制层返回结果封装实体
 * @param <T>
 * @author xbcai
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> Result<T> success(T data){
        return new Result<>(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMessage(),data);
    }

    public static <T> Result<T> success(T data,ResultEnum resultEnum){
        return new Result<>(resultEnum.getCode(),resultEnum.getMessage(),data);
    }

    public static  Result success(){
        return new Result(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMessage());
    }

    public static Result fail(){
        return new Result(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMessage());
    }
    public static <T> Result<T> fail(T data){
        return new Result<>(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMessage(),data);
    }

    public static <T> Result<T> fail(int code,String message,T data){
        return new Result<>(code,message,data);
    }

    public static <T> Result<T> fail(T data,ResultEnum resultEnum){
        return new Result<>(resultEnum.getCode(),resultEnum.getMessage(),data);
    }

    public static  Result fail(ResultEnum resultEnum){
        return new Result(resultEnum.getCode(),resultEnum.getMessage());
    }


}
