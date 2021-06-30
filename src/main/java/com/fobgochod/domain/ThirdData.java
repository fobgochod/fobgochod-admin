package com.fobgochod.domain;

/**
 * 请求第三方信息
 * 响应错误信息
 *
 * @author zhouxiao
 * @date 2020/6/18
 */
public class ThirdData {

    private Object response;
    private String url;
    private Integer status;

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
