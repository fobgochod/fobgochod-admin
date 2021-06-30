package com.fobgochod.domain;

/**
 * 标准输出
 *
 * @author zhouxiao
 * @date 2020/3/5
 */
public class StdData {

    public static final StdData OK = new StdData(200, "success", true);

    private int code;
    private String message;
    private Object data;
    private boolean success;

    public StdData() {
    }

    public StdData(boolean success) {
        this.success = success;
    }

    public StdData(int code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static StdData ok() {
        return OK;
    }

    public static StdData of(boolean success) {
        return new StdData(success);
    }

    public static StdData ofSuccess(Object data) {
        StdData stdData = new StdData(200, "success", true);
        stdData.setData(data);
        return stdData;
    }

    public static StdData ofFail(int code, String message) {
        return new StdData(code, message, false);
    }
}
