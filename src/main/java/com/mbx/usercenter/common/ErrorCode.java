package com.mbx.usercenter.common;

public enum ErrorCode {

    /**
     * 参数错误
     */
    PARAMS_ERROR(40000, "参数错误", "参数错误"),
    /**
     * 空指针错误
     */
    NULL_ERROR(40001, "空指针错误", "空指针错误"),
    /**
     * 未找到
     */
    NOT_FOUND_ERROR(40400, "未找到", "未找到"),
    /**
     * 无权限
     */
    FORBIDDEN_ERROR(40300, "无权限", "无权限"),
    /**
     * 系统内部错误
     */
    SYSTEM_ERROR(50000, "系统内部异常", "系统内部异常"),
    /**
     * 操作失败
     */
    OPERATION_ERROR(50001, "操作失败", "操作失败");

    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
