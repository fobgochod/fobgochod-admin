package com.fobgochod.api.medicine;

import com.fobgochod.auth.domain.LoginUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.medicine.MedicineVO;
import com.fobgochod.domain.medicine.MyMedicine;
import com.fobgochod.entity.admin.Medicine;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.MedicineRepository;
import com.fobgochod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Bucket 存储区
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/medicines")
public class MedicineApi {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MedicineRepository medicineRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Medicine body) {
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
    public ResponseEntity<?> search(@RequestBody(required = false) Page body) {
        medicineRepository.findUserIds();
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

    @PostMapping("/me")
    public ResponseEntity<?> medicines(@RequestBody Medicine body) {
        User user = userRepository.findByCode(body.getUserId());
        List<Medicine> medicines = medicineRepository.findByUserId(body.getUserId());

        List<MedicineVO> medicineVOS = new ArrayList<>();
        medicines.forEach(m -> {
            MedicineVO vo = new MedicineVO();
            vo.doBackward(m);
            medicineVOS.add(vo);
        });

        MyMedicine myMedicine = new MyMedicine();
        myMedicine.setUserId(user.getCode());
        myMedicine.setUserName(user.getName());
        myMedicine.setMedicines(medicineVOS);
        return ResponseEntity.ok(myMedicine);
    }
}
