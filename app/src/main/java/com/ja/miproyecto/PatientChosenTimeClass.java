package com.ja.miproyecto;

public class PatientChosenTimeClass {
    private String time;
    private int payment,status;

    public PatientChosenTimeClass(String time, int payment, int status) {
        this.time = time;
        this.payment = payment;
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}