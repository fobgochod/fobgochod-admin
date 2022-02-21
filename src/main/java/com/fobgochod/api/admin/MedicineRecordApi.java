package com.fobgochod.api.admin;

import com.fobgochod.auth.domain.JwtUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.admin.Medicine;
import com.fobgochod.entity.admin.MedicineRecord;
import com.fobgochod.repository.MedicineRecordRepository;
import com.fobgochod.repository.MedicineRepository;
import com.fobgochod.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bucket 存储区
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/medicine/records")
public class MedicineRecordApi {

    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineRecordRepository medicineRecordRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MedicineRecord body) {
        String id = medicineRecordRepository.insert(body);
        return ResponseEntity.ok(medicineRecordRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        medicineRecordRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody MedicineRecord body) {
        medicineRecordRepository.update(body);
        return ResponseEntity.ok(medicineRecordRepository.findById(body.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(medicineRecordRepository.findById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page body) {
        return ResponseEntity.ok(medicineRecordRepository.findByPage(body));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(medicineRecordRepository.deleteByIdIn(body.getIds()));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        medicineRecordRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/eat")
    public ResponseEntity<?> eat(@RequestAttribute(FghConstants.HTTP_HEADER_USER_INFO_KEY) JwtUser userInfo) {
        List<Medicine> medicines = medicineRepository.findByUserId(userInfo.getUsername());
        medicines.forEach(medicine -> {
            String type = DateUtils.getType();
            MedicineRecord medicineRecord = medicineRecordRepository.findByMedicineIdAndType(medicine.getId(), type);
            if (medicineRecord != null) {
                return;
            }
            MedicineRecord record = new MedicineRecord();
            record.setMedicineId(medicine.getId());
            record.setType(type);
            switch (type) {
                case DateUtils.MORNING:
                    record.setSlice(medicine.getMorning());
                    break;
                case DateUtils.NOON:
                    record.setSlice(medicine.getNoon());
                    break;
                case DateUtils.NIGHT:
                    record.setSlice(medicine.getNight());
                    break;
            }
            record.setTime(LocalDateTime.now());
            medicineRecordRepository.insert(record);

            medicine.setTotal(medicine.getTotal() - record.getSlice());
            medicine.setRemain(medicine.getTotal() / (medicine.getMorning() + medicine.getNoon() + medicine.getNight()));
            medicineRepository.update(medicine);
        });
        return ResponseEntity.ok().build();
    }
}
