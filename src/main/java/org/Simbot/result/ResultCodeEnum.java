package org.Simbot.result;

public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    LOGIN_ERROR(208, "认证失败"),
    FILE_SIZE_LIMIT_EXCEEDED(209, "上传文件大小超出限制"),
    MISSING_PARAMETER(210, "缺少参数"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "方法不允许"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    CUSTOM_CODE_ONE(600, "自定义状态码一"),
    CUSTOM_CODE_TWO(601, "自定义状态码二");

    private final int code;
    private final String message;

    ResultCodeEnum(int code, String message) {
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
