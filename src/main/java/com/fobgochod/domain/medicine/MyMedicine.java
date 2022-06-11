package com.fobgochod.domain.medicine;

import java.util.List;

/**
 * 我的药物信息
 *
 * @author Xiao
 * @date 2022/2/24 22:56
 */
public class MyMedicine {

    private String userCode;
    private String userName;
    private List<MedicineVO> medicines;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<MedicineVO> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<MedicineVO> medicines) {
        this.medicines = medicines;
    }
}
