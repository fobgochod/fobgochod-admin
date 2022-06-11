package com.fobgochod.service.message.sms.impl;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.EnvProperties;
import com.fobgochod.domain.enumeration.ProfileEnum;
import com.fobgochod.domain.medicine.MedicType;
import com.fobgochod.entity.SmsRecord;
import com.fobgochod.exception.SystemException;
import com.fobgochod.repository.SmsRecordRepository;
import com.fobgochod.serializer.Constants;
import com.fobgochod.service.message.sms.AliyunSms;
import com.fobgochod.service.message.sms.AliyunSmsService;
import com.fobgochod.util.CaptchaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AliyunSmsServiceImpl implements AliyunSmsService {

    @Autowired
    private EnvProperties env;
    @Autowired
    private SmsRecordRepository smsRecordRepository;
    @Autowired
    private com.aliyun.dysmsapi20170525.Client smsClient;

    @Override
    public void test(String telephone, String name) {
        String templateParam = String.format("{\"name\":\"%s\"}", name);
        SmsRecord smsRecord = new SmsRecord();
        smsRecord.setTelephone(telephone);
        smsRecord.setType(AliyunSms.TemplateCode.TC_TEST.getName());
        smsRecord.setTemplateCode(AliyunSms.TemplateCode.TC_TEST.getCode());
        smsRecord.setTemplateParam(templateParam);
        smsRecord.setSendDate(LocalDateTime.now());

        sendSms(smsRecord);
    }

    @Override
    public void captcha(String telephone) {
        String code = CaptchaUtils.generateCaptcha();
        String templateParam = String.format("{\"code\":\"%s\"}", code);

        SmsRecord smsRecord = new SmsRecord();
        smsRecord.setTelephone(telephone);
        smsRecord.setType(AliyunSms.TemplateCode.TC_CAPTCHA.getName());
        smsRecord.setTemplateCode(AliyunSms.TemplateCode.TC_CAPTCHA.getCode());
        smsRecord.setTemplateParam(templateParam);
        smsRecord.setSendDate(LocalDateTime.now());
        smsRecord.setCaptchaCode(code);
        smsRecord.setCaptchaExpire(LocalDateTime.now().plusMinutes(5));

        sendSms(smsRecord);
    }

    @Override
    public void medicine(String telephone, String userName, MedicType type) {
        String templateParam = String.format("{\"name\":\"%s\",\"time\":\"%s\"}", userName, type.getName());

        SmsRecord smsRecord = new SmsRecord();
        smsRecord.setTelephone(telephone);
        smsRecord.setType(AliyunSms.TemplateCode.TC_MEDICINE.getName());
        smsRecord.setTemplateCode(AliyunSms.TemplateCode.TC_MEDICINE.getCode());
        smsRecord.setTemplateParam(templateParam);
        smsRecord.setSendDate(LocalDateTime.now());

        sendSms(smsRecord);
    }

    @Override
    public void registration(String telephone, String userName, int remain) {
        String templateParam = String.format("{\"name\":\"%s\",\"remain\":\"%s\"}", userName, remain);

        SmsRecord smsRecord = new SmsRecord();
        smsRecord.setTelephone(telephone);
        smsRecord.setType(AliyunSms.TemplateCode.TC_REGISTRATION.getName());
        smsRecord.setTemplateCode(AliyunSms.TemplateCode.TC_REGISTRATION.getCode());
        smsRecord.setTemplateParam(templateParam);
        smsRecord.setSendDate(LocalDateTime.now());

        sendSms(smsRecord);
    }

    @Override
    public void birthday(String telephone, String userName, LocalDate birth) {
        String templateParam = String.format("{\"name\":\"%s\",\"birth\":\"%s\"}", userName, birth.format(Constants.DATE_FORMATTER));

        SmsRecord smsRecord = new SmsRecord();
        smsRecord.setTelephone(telephone);
        smsRecord.setType(AliyunSms.TemplateCode.TC_BIRTHDAY.getName());
        smsRecord.setTemplateCode(AliyunSms.TemplateCode.TC_BIRTHDAY.getCode());
        smsRecord.setTemplateParam(templateParam);
        smsRecord.setSendDate(LocalDateTime.now());

        sendSms(smsRecord);
    }

    @Override
    public void sendSms(SmsRecord smsRecord) {
        if (ProfileEnum.PROD.name().equalsIgnoreCase(env.getActive())) {
            smsRecord.setSignName(AliyunSms.SIGN_NAME_ROBO);
        } else {
            smsRecord.setSignName(AliyunSms.SIGN_NAME);
        }
        try {
            SendSmsRequest sendSmsRequest = new SendSmsRequest().setPhoneNumbers(smsRecord.getTelephone())
                    .setSignName(smsRecord.getSignName())
                    .setTemplateCode(smsRecord.getTemplateCode())
                    .setTemplateParam(smsRecord.getTemplateParam());
            SendSmsResponseBody response = smsClient.sendSms(sendSmsRequest).getBody();
            smsRecord.setStatus(FghConstants.OK.equals(response.getCode()));
            smsRecord.setSmsCode(response.getCode());
            smsRecord.setSmsMessage(response.getMessage());
            smsRecordRepository.insert(smsRecord);
        } catch (Exception e) {
            smsRecord.setStatus(false);
            smsRecord.setRemark(e.getMessage());
            smsRecordRepository.insert(smsRecord);
            throw new SystemException(e.getMessage());
        }
    }
}
