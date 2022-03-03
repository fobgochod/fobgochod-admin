package com.fobgochod.api.medicine;

import com.fobgochod.domain.GroupBy;
import com.fobgochod.domain.medicine.MedicType;
import com.fobgochod.domain.medicine.MedicineVO;
import com.fobgochod.domain.medicine.MyMedicine;
import com.fobgochod.entity.admin.Medicine;
import com.fobgochod.entity.admin.MedicineRecord;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.MedicineRecordRepository;
import com.fobgochod.repository.MedicineRepository;
import com.fobgochod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bucket 存储区
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/medicines")
public class MyMedicineApi {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineRecordRepository medicineRecordRepository;

    @PostMapping("/me")
    public ResponseEntity<?> medicines(@RequestBody Medicine body) {
        User user = userRepository.findByCode(body.getUserId());
        List<Medicine> medicines = medicineRepository.findByUserId(body.getUserId());
        List<MedicineRecord> medicineRecords = medicineRecordRepository.findByMedicineIdIn(medicines.stream().map(Medicine::getId).collect(Collectors.toList()));
        Map<String, List<MedicineRecord>> recordMap = medicineRecords.stream().collect(Collectors.groupingBy(MedicineRecord::getMedicineId));


        List<MedicineVO> medicineVOS = new ArrayList<>();
        medicines.forEach(m -> {
            MedicineVO vo = new MedicineVO();
            vo.doBackward(m);
            List<String> recordTypes = recordMap.getOrDefault(m.getId(), Collections.emptyList())
                    .stream().map(MedicineRecord::getType).collect(Collectors.toList());
            vo.setMorningB(recordTypes.contains(MedicType.MORNING.getName()));
            vo.setNoonB(recordTypes.contains(MedicType.NOON.getName()));
            vo.setNightB(recordTypes.contains(MedicType.NIGHT.getName()));
            medicineVOS.add(vo);
        });

        MyMedicine myMedicine = new MyMedicine();
        myMedicine.setUserId(user.getCode());
        myMedicine.setUserName(user.getName());
        myMedicine.setMedicines(medicineVOS);
        return ResponseEntity.ok(myMedicine);
    }

    @PostMapping("/eat")
    public ResponseEntity<?> eat(@RequestBody Medicine body) {
        List<Medicine> medicines = medicineRepository.findByUserId(body.getUserId());
        medicines.forEach(medicine -> {
            MedicType type = MedicType.type();
            MedicineRecord medicineRecord = medicineRecordRepository.findRecord(medicine.getId(), type.getName());
            if (medicineRecord != null) {
                return;
            }
            float slice;
            if (type == MedicType.MORNING) {
                slice = medicine.getMorning();
            } else if (type == MedicType.NOON) {
                slice = medicine.getNoon();
            } else if (type == MedicType.NIGHT) {
                slice = medicine.getNight();
            } else {
                slice = 0;
            }
            if (slice == 0) {
                return;
            }
            MedicineRecord record = new MedicineRecord();
            record.setMedicineId(medicine.getId());
            record.setType(type.getName());
            record.setSlice(-slice);
            record.setDate(LocalDate.now());
            record.setTime(LocalTime.now());
            medicineRecordRepository.insert(record);
        });


        List<GroupBy> medicineCounts = medicineRecordRepository.findMedicineCounts();
        Map<String, Float> medicineCountMap = medicineCounts.stream().collect(Collectors.toMap(GroupBy::getId, GroupBy::getSum));
        medicines.forEach(medicine -> {
            medicine.setTotal(medicineCountMap.get(medicine.getId()));
            Float day = medicine.getMorning() + medicine.getNoon() + medicine.getNight();
            medicine.setRemain((int) (medicine.getTotal() / day));
            medicineRepository.update(medicine);
        });
        return ResponseEntity.ok().build();
    }
}
