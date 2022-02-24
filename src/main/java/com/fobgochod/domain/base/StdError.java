package com.fobgochod.domain.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fobgochod.domain.enumeration.ErrorType;
import com.fobgochod.exception.FghException;

import java.time.LocalDateTime;

/**
 * 标准错误
 *
 * @author seven
 * @date 2020/6/7
 */
@JsonInclude
public class StdError {

    private LocalDateTime timestamp;
    private ErrorType type;
    private Integer code;
    private String message;
    private String path;
    private boolean success;

    private StdError() {
        this.timestamp = LocalDateTime.now();
    }

    public static StdError of() {
        return new StdError();
    }

    public static StdError of(FghException e) {
        StdError stdError = StdError.of();
        stdError.setType(e.getErrorType());
        stdError.setCode(e.getHandler().getCode());
        stdError.setMessage(e.getHandler().getMessage());
        return stdError;
    }

    public static StdError of(FghException e, String path) {
        StdError stdError = StdError.of(e);
        stdError.setPath(path);
        return stdError;
    }

    public static StdError of(ErrorType errorType, Integer code, String message, String path) {
        StdError stdError = StdError.of();
        stdError.setType(errorType);
        stdError.setCode(code);
        stdError.setMessage(message);
        stdError.setPath(path);
        return stdError;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ErrorType getType() {
        return type;
    }

    public void setType(ErrorType type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "StdError{" + "timestamp=" + timestamp + ", type=" + type + ", code=" + code + ", message='" + message + '\'' + ", path='" + path + '\'' + ", success=" + success + '}';
    }
}
