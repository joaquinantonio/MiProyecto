package com.ja.miproyecto;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import javax.annotation.Nullable;

public class PatientAppointmentClass {

    private String dname, email, date, time, specialist;
    private int payment_status, cancelled;

    public PatientAppointmentClass(String dname, String email, String date, String time, String specialist, int payment_status, int cancelled) {
        this.dname = dname;
        this.email = email;
        this.date = date;
        this.time = time;
        this.specialist = specialist;
        this.payment_status = payment_status;
        this.cancelled = cancelled;
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

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public int getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(int payment_status) {
        this.payment_status = payment_status;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }
}


/*
public class PatientAppointmentClass extends AppCompatActivity {

    private String dname, email, date, time, specialist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view or perform other initialization if needed
        // setContentView(R.layout.activity_patient_appointment);

        // Retrieve data from Intent extras
        String dname = getIntent().getStringExtra("dname");
        String email = getIntent().getStringExtra("email");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String specialist = getIntent().getStringExtra("specialist");

        // Use the data as needed
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
*/