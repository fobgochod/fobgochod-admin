package com.fobgochod.exception;

import com.fobgochod.domain.base.ErrorHandler;
import com.fobgochod.util.ExceptionUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 标准错误
 *
 * @author seven
 * @date 2020/6/7
 */
public class StdError extends AppError {

    private String sourceId;
    private String errorType;
    private String errorCode;
    private String errorMessage;
    private DebugInfo debugInfo;

    public StdError() {
        super();
    }

    public StdError(String sourceId) {
        this();
        this.sourceId = sourceId;
    }

    public static StdError of() {
        return new StdError();
    }

    public static StdError of(String sourceId) {
        return new StdError(sourceId);
    }

    public static StdError of(String sourceId, FghException e) {
        StdError stdError = new StdError(sourceId);
        stdError.setErrorType(e.getErrorType().name());
        stdError.setErrorCode(e.getCode());
        stdError.setErrorMessage(e.getMessage());
        return stdError;
    }

    public static StdError of(String sourceId, FghException e, String path) {
        StdError stdError = of(sourceId, e);
        stdError.setPath(path);
        return stdError;
    }

    public static StdError of(String sourceId, String errorType, ErrorHandler errorHandler, String path) {
        StdError stdError = new StdError();
        stdError.setSourceId(sourceId);
        stdError.setErrorType(errorType);
        stdError.setErrorCode(errorHandler.getErrorCode());
        stdError.setErrorMessage(errorHandler.getErrorMessage());
        stdError.setPath(path);
        return stdError;
    }

    public static DebugInfo getDebugInfo(HttpServletRequest request, Exception e) {
        DebugInfo debugInfo = new DebugInfo();
        debugInfo.setMethod(request.getMethod());
        debugInfo.setQueryParams(request.getQueryString());
        debugInfo.setStackTrace(ExceptionUtils.getStackTrace(e));
        return debugInfo;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public DebugInfo getDebugInfo() {
        return debugInfo;
    }

    public void setDebugInfo(DebugInfo debugInfo) {
        this.debugInfo = debugInfo;
    }

    /**
     * 调试信息
     */
    private static class DebugInfo {
        private String method;
        private String queryParams;
        private String stackTrace;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getQueryParams() {
            return queryParams;
        }

        public void setQueryParams(String queryParams) {
            this.queryParams = queryParams;
        }

        public String getStackTrace() {
            return stackTrace;
        }

        public void setStackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
        }
    }
}
