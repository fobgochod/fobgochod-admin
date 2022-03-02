package com.fobgochod.entity.admin;

import com.fobgochod.entity.BaseEntity;
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

    private String telephone;
    private boolean status;
    private LocalDateTime sendDate;
    private String signName;
    private String templateCode;
    private String templateParam;
    private String captchaCode;
    private LocalDateTime captchaExpire;
    private String smsCode;
    private String smsMessage;
    private String remark;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
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
