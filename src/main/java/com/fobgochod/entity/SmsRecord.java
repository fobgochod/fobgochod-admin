package com.fobgochod.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 短信发送记录
 *
 * @author Xiao
 * @date 2022/3/1 0:16
 */
@Document("sms_record")
public class SmsRecord extends BaseEntity {

    private String type;
    private String telephone;
    private Boolean status;
    private LocalDateTime sendDate;
    private String signName;
    private String templateCode;
    private String templateParam;
    private String captchaCode;
    private LocalDateTime captchaExpire;
    private String smsCode;
    private String smsMessage;
    private String remark;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public LocalDateTime getCaptchaExpire() {
        return captchaExpire;
    }

    public void setCaptchaExpire(LocalDateTime captchaExpire) {
        this.captchaExpire = captchaExpire;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
