package com.mbx.usercenter.common;

/**
 * 封装返回结果
 */
public class ResultUtils {

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     */
    public static <T> BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }


    public static BaseResponse error(ErrorCode errorCode, String message, String s) {
        return new BaseResponse(errorCode.getCode(), null, message, s);
    }

    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }
}
