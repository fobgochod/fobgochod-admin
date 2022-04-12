package com.fobgochod.api.medicine;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.admin.MedicineRecord;
import com.fobgochod.repository.MedicineRecordRepository;
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
    public ResponseEntity<?> search(@RequestBody Page<MedicineRecord> body) {
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
}
