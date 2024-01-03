package com.ja.miproyecto;

public class AppointmentNotif {

    String appointment_text,date,time,Questions,phone,pname;

    public AppointmentNotif(String appointment_text, String date, String time, String questions, String phone, String pname) {
        this.appointment_text = appointment_text;
        this.date = date;
        this.time = time;
        this.Questions = questions;
        this.phone = phone;
        this.pname = pname;
    }

    public String getAppointment_text() {
        return appointment_text;
    }

    public String getPname() {
        return pname;
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

    public String getQuestions() {
        return Questions;
    }

    public void setQuestions(String questions) {
        Questions = questions;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}