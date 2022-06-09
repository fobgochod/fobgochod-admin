package com.fobgochod.api.admin;

import com.fobgochod.auth.holder.AuthoredUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.SmsRecord;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.SmsRecordRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.message.sms.AliyunSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * SmsRecordApi.java
 *
 * @author Xiao
 * @date 2022/3/2 23:52
 */
@RestController
@RequestMapping("/sms/records")
public class SmsRecordController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AliyunSmsService aliyunSmsService;
    @Autowired
    private SmsRecordRepository smsRecordRepository;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody SmsRecord body) {
        String id = smsRecordRepository.insert(body);
        return ResponseEntity.ok(smsRecordRepository.findById(id));
    }

    @PostMapping("/del")
    public ResponseEntity<?> delete(@RequestBody SmsRecord body) {
        smsRecordRepository.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody SmsRecord body) {
        smsRecordRepository.update(body);
        return ResponseEntity.ok(smsRecordRepository.findById(body.getId()));
    }

    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody SmsRecord body) {
        return ResponseEntity.ok(smsRecordRepository.findById(body.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page<SmsRecord> body) {
        return ResponseEntity.ok(smsRecordRepository.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        smsRecordRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(smsRecordRepository.deleteByIdIn(body.getIds()));
    }

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestAttribute(FghConstants.HTTP_HEADER_USER_INFO) AuthoredUser authoredUser) {
        User user = userRepository.findByCode(authoredUser.getUserId());
        aliyunSmsService.test(user.getTelephone(), user.getName());
        return ResponseEntity.ok().build();
    }
}
