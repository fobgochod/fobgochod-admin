package com.fobgochod.exception;

import java.time.LocalDateTime;

/**
 * 错误信息
 *
 * @author seven
 * @date 2020/5/16
 */
@Deprecated
public class AppError {

    public static final AppError FAIL = new AppError(500, "fail");

    private LocalDateTime timestamp;
    private Integer code;
    private String message;
    private String path;
    private boolean success;

    public AppError() {
        this.timestamp = LocalDateTime.now();
        this.success = false;
    }

    public AppError(String message) {
        this();
        this.code = 500;
        this.message = message;
    }

    public AppError(String message, String path) {
        this(message);
        this.path = path;
    }

    public AppError(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public AppError(int code, String message, String path) {
        this(code, message);
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static AppError fail() {
        return FAIL;
    }

    public static AppError of(String message) {
        return new AppError(message);
    }

    public static AppError of(String message, String path) {
        return new AppError(message, path);
    }

    public static AppError of(int code, String message) {
        return new AppError(code, message);
    }

    public static AppError of(int code, String message, String path) {
        return new AppError(code, message, path);
    }
}
