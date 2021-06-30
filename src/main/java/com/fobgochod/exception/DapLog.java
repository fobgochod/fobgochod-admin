package com.fobgochod.exception;

import com.fobgochod.util.TokenUtils;

import javax.servlet.http.HttpServletRequest;

public class DapLog {

    private String handler;
    private String message;
    private String method;
    private String path;
    private String userToken;

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public static String getLog(String message, String path) {
        DapLog dapLog = new DapLog();
        dapLog.setHandler(DapExceptionHandler.Handler.UNEXPECTED.getName());
        dapLog.setMessage(message);
        dapLog.setMethod(null);
        dapLog.setPath(path);
        dapLog.setUserToken(TokenUtils.getToken());
        return dapLog.toString();
    }

    public static String getLog(String handler, String message, HttpServletRequest req) {
        DapLog dapLog = new DapLog();
        dapLog.setHandler(handler);
        dapLog.setMessage(message);
        dapLog.setMethod(req.getMethod());
        dapLog.setPath(req.getRequestURI());
        dapLog.setUserToken(TokenUtils.getToken());
        return dapLog.toString();
    }

    @Override
    public String toString() {
        return "{"
                + "\"handler\":\""
                + handler + '\"'
                + ",\"message\":\""
                + message + '\"'
                + ",\"method\":\""
                + method + '\"'
                + ",\"path\":\""
                + path + '\"'
                + ",\"userToken\":\""
                + userToken + '\"'
                + "}";
    }
}
