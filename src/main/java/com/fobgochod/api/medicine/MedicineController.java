package com.fobgochod.api.medicine;

import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.medicine.Medicine;
import com.fobgochod.repository.MedicineRepository;
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
@RequestMapping("/medicines")
public class MedicineController {

    @Autowired
    private MedicineRepository medicineRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Medicine body) {
        body.setStatus(false);
        String id = medicineRepository.insert(body);
        return ResponseEntity.ok(medicineRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        medicineRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody Medicine body) {
        medicineRepository.update(body);
        return ResponseEntity.ok(medicineRepository.findById(body.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(medicineRepository.findById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page<Medicine> body) {
        if (!FghConstants.ADMIN_USER.equals(UserUtil.getUserId())) {
            Medicine medicine = body.getCond();
            medicine.setUserId(UserUtil.getUserId());
        }
        return ResponseEntity.ok(medicineRepository.findByPage(body));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(medicineRepository.deleteByIdIn(body.getIds()));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        medicineRepository.dropCollection();
        return ResponseEntity.ok().build();
    }
}
