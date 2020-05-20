package com.xbcai.enumeration;

/**
 * 全局返回码定义
 */
public enum ResultEnum {
    /**
     * 成功
     */
    SUCCESS(200, "OK"),
    /**
     *请求失败
     */
    FAIL(400, "请求失败"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未经授权的"),
    /**
     * 被禁止调用
     */
    FORBIDDEN(403, "被禁止的"),
    /**
     * 访问不到
     */
    NOT_FOUND(404, "找不到该资源"),
    /**
     * 不允许的方法
     */
    METHOD_NOT_ALLOWED(405, "不允许的方法"),
    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),
    /**
     * 冲突
     */
    CONFLICT(409, "冲突"),
    /**
     * 不存在
     */
    GONE(410, "不存在"),
    /**
     * 有效载荷太大
     */
    PAYLOAD_TOO_LARGE(413, "有效载荷太大"),
    /**
     * 请求实体太大
     */
    REQUEST_ENTITY_TOO_LARGE(413, "请求实体太大"),
    /**
     * 异常失败
     */
    EXPECTATION_FAILED(417, "异常失败"),
    /**
     * 请求过于频繁
     */
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    /**
     * 内部服务器错误
     */
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),
    /**
     * 坏网关
     */
    BAD_GATEWAY(502, "坏网关"),
    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    /**
     * 网关超时
     */
    GATEWAY_TIMEOUT(504, "网关超时"),
    /**
     * 需要网络身份验证
     */
    NETWORK_AUTHENTICATION_REQUIRED(511, "需要网络身份验证"),
    ;
    private int code;
    private String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }



    public String getMessage() {
        return message;
    }

}
