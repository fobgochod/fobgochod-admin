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

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody Medicine body) {
        body.setStatus(false);
        String id = medicineRepository.insert(body);
        return ResponseEntity.ok(medicineRepository.findById(id));
    }

    @PostMapping("/del")
    public ResponseEntity<?> delete(@RequestBody Medicine body) {
        medicineRepository.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody Medicine body) {
        medicineRepository.update(body);
        return ResponseEntity.ok(medicineRepository.findById(body.getId()));
    }

    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody Medicine body) {
        return ResponseEntity.ok(medicineRepository.findById(body.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Page<Medicine> body) {
        if (!FghConstants.ADMIN_USER.equals(UserUtil.getUserCode())) {
            Medicine medicine = body.getFilter().getEq();
            medicine.setUserCode(UserUtil.getUserCode());
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
