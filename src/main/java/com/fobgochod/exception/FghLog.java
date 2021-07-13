package com.fobgochod.exception;

import com.fobgochod.util.TokenUtils;

import javax.servlet.http.HttpServletRequest;

public class FghLog {

    private String handler;
    private String message;
    private String method;
    private String path;
    private String token;

    public static String getLog(String message, String path) {
        FghLog fghLog = new FghLog();
        fghLog.setHandler(FghExceptionHandler.Handler.UNEXPECTED.getName());
        fghLog.setMessage(message);
        fghLog.setMethod(null);
        fghLog.setPath(path);
        fghLog.setToken(TokenUtils.getToken());
        return fghLog.toString();
    }

    public static String getLog(String handler, String message, HttpServletRequest req) {
        FghLog fghLog = new FghLog();
        fghLog.setHandler(handler);
        fghLog.setMessage(message);
        fghLog.setMethod(req.getMethod());
        fghLog.setPath(req.getRequestURI());
        fghLog.setToken(TokenUtils.getToken());
        return fghLog.toString();
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
                + ",\"token\":\""
                + token + '\"'
                + "}";
    }
}
