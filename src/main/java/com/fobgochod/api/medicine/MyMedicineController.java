package com.fobgochod.api.medicine;

import com.fobgochod.domain.GroupBy;
import com.fobgochod.domain.medicine.EatMedicine;
import com.fobgochod.domain.medicine.MedicineItemVO;
import com.fobgochod.domain.medicine.MedicineVO;
import com.fobgochod.domain.medicine.MyMedicine;
import com.fobgochod.entity.admin.User;
import com.fobgochod.entity.spda.Medicine;
import com.fobgochod.entity.spda.MedicineItem;
import com.fobgochod.entity.spda.MedicineRecord;
import com.fobgochod.repository.MedicineItemRepository;
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
public class MyMedicineController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineItemRepository medicineItemRepository;
    @Autowired
    private MedicineRecordRepository medicineRecordRepository;

    @PostMapping("/me")
    public ResponseEntity<?> medicines(@RequestBody Medicine body) {
        User user = userRepository.findByCode(body.getUserId());
        List<Medicine> medicines = medicineRepository.findByUserId(body.getUserId(), false);
        List<String> medicineIds = medicines.stream().map(Medicine::getId).collect(Collectors.toList());
        List<MedicineRecord> medicineRecords = medicineRecordRepository.findByMedicineIdIn(medicineIds);
        Map<String, List<MedicineRecord>> recordMap = medicineRecords.stream().collect(Collectors.groupingBy(MedicineRecord::getMedicineId));

        List<MedicineItem> medicineItems = medicineItemRepository.findByMedicineIdIn(medicineIds);
        Map<String, List<MedicineItem>> itemMap = medicineItems.stream().collect(Collectors.groupingBy(MedicineItem::getMedicineId));

        List<MedicineVO> medicineVOS = new ArrayList<>();
        medicines.forEach(m -> {
            List<String> recordTypes = recordMap.getOrDefault(m.getId(), Collections.emptyList()).stream().map(MedicineRecord::getType).collect(Collectors.toList());

            List<MedicineItemVO> itemVOS = new ArrayList<>();
            itemMap.getOrDefault(m.getId(), Collections.emptyList()).forEach(item -> {
                MedicineItemVO itemVO = new MedicineItemVO();
                itemVO.doBackward(item);
                itemVO.setState(recordTypes.contains(item.getType()));
                itemVOS.add(itemVO);
            });

            MedicineVO vo = new MedicineVO();
            vo.doBackward(m);
            vo.setItems(itemVOS);
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
        List<Medicine> medicines = medicineRepository.findByUserId(body.getUserId(), false);
        medicines.forEach(medicine -> {
            MedicineItem item = medicineItemRepository.findItem(medicine.getId());
            if (item == null || item.getSlice() <= 0) {
                return;
            }
            eatMedicine(medicine.getId(), item, LocalDate.now(), LocalTime.now());
        });

        calcTotalMedicine(medicines);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/eat/day")
    public ResponseEntity<?> eat(@RequestBody EatMedicine body) {
        List<Medicine> medicines = medicineRepository.findByUserId(body.getUserId(), false);
        medicines.forEach(medicine -> {
            List<MedicineItem> items = medicineItemRepository.findItems(medicine.getId());
            items.forEach(item -> {
                if (item.getSlice() <= 0) {
                    return;
                }
                eatMedicine(medicine.getId(), item, body.getDate(), item.getStart());
            });
        });
        calcTotalMedicine(medicines);
        return ResponseEntity.ok().build();
    }

    private void eatMedicine(String medicineId, MedicineItem item, LocalDate date, LocalTime time) {
        MedicineRecord record = medicineRecordRepository.findRecord(medicineId, item.getType(), date);
        if (record != null) {
            return;
        }
        MedicineRecord medicineRecord = new MedicineRecord();
        medicineRecord.setMedicineId(medicineId);
        medicineRecord.setType(item.getType());
        medicineRecord.setSlice(-item.getSlice());
        medicineRecord.setDate(date);
        medicineRecord.setTime(time);
        medicineRecordRepository.insert(medicineRecord);
    }

    private void calcTotalMedicine(List<Medicine> medicines) {
        List<GroupBy> medicineCounts = medicineRecordRepository.findMedicineCounts(medicines.stream().map(Medicine::getId).collect(Collectors.toList()));
        Map<String, Float> medicineCountMap = medicineCounts.stream().collect(Collectors.toMap(GroupBy::getId, GroupBy::getSum));
        medicines.forEach(medicine -> {
            medicine.setTotal(medicineCountMap.getOrDefault(medicine.getId(), 0f));
            Float day = medicine.getMorning() + medicine.getNoon() + medicine.getNight();
            medicine.setRemain((int) (medicine.getTotal() / day));
            medicineRepository.update(medicine);
        });
    }
}
