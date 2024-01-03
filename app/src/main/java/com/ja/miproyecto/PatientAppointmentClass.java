package com.ja.miproyecto;

public class PatientAppointmentClass {
    private String dname, email, date, time, specialist;

    public PatientAppointmentClass(String dname, String email, String date, String time, String specialist, int payment_status, int cancelled) {
        this.dname = dname;
        this.email = email;
        this.date = date;
        this.time = time;
        this.specialist = specialist;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}