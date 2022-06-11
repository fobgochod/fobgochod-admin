package com.fobgochod.api.medicine;

import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.medicine.MedicineRecord;
import com.fobgochod.repository.MedicineRecordRepository;
import com.fobgochod.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Bucket 存储区
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/medicine/records")
public class MedicineRecordController {

    @Autowired
    private MedicineRecordRepository medicineRecordRepository;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody MedicineRecord body) {
        String id = medicineRecordRepository.insert(body);
        return ResponseEntity.ok(medicineRecordRepository.findById(id));
    }

    @PostMapping("/del")
    public ResponseEntity<?> delete(@RequestBody MedicineRecord body) {
        medicineRecordRepository.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody MedicineRecord body) {
        medicineRecordRepository.update(body);
        return ResponseEntity.ok(medicineRecordRepository.findById(body.getId()));
    }

    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody MedicineRecord body) {
        return ResponseEntity.ok(medicineRecordRepository.findById(body.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Page<MedicineRecord> body) {
        if (!FghConstants.ADMIN_USER.equals(UserUtil.getUserCode())) {
            MedicineRecord medicine = body.getFilter().getEq();
            medicine.setUserCode(UserUtil.getUserCode());
        }
        return ResponseEntity.ok(medicineRecordRepository.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        medicineRecordRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(medicineRecordRepository.deleteByIdIn(body.getIds()));
    }
}
