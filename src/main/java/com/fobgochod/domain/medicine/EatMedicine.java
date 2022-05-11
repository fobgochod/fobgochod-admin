package com.fobgochod.domain.medicine;

import java.time.LocalDate;

public class EatMedicine {

    private String userId;
    private LocalDate date;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
