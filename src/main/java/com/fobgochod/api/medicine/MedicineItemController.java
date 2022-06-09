package com.fobgochod.api.medicine;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.medicine.MedicineItem;
import com.fobgochod.repository.MedicineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * MedicineItemController.java
 *
 * @author Xiao
 * @date 2022/5/10 22:47
 */
@RestController
@RequestMapping("/medicine/items")
public class MedicineItemController {

    @Autowired
    private MedicineItemRepository medicineItemRepository;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody MedicineItem body) {
        String id = medicineItemRepository.insert(body);
        return ResponseEntity.ok(medicineItemRepository.findById(id));
    }

    @PostMapping("/del")
    public ResponseEntity<?> delete(@RequestBody MedicineItem body) {
        medicineItemRepository.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody MedicineItem body) {
        medicineItemRepository.update(body);
        return ResponseEntity.ok(medicineItemRepository.findById(body.getId()));
    }

    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody MedicineItem body) {
        return ResponseEntity.ok(medicineItemRepository.findById(body.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Page<MedicineItem> body) {
        return ResponseEntity.ok(medicineItemRepository.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        medicineItemRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(medicineItemRepository.deleteByIdIn(body.getIds()));
    }
}
