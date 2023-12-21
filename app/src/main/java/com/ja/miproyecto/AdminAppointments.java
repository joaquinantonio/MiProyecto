package com.ja.miproyecto;

public class AdminAppointments {

    private String Date,Time,Doctor,Patient_phone ;

    public AdminAppointments() {
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDoctor() {
        return Doctor;
    }

    public void setDoctor(String doctor) {
        Doctor = doctor;
    }

    public String getPatient_phone() {
        return Patient_phone;
    }

    public void setPatient_phone(String patient_phone) {
        Patient_phone = patient_phone;
    }
}
