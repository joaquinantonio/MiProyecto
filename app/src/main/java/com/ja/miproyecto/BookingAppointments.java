package com.ja.miproyecto;

public class BookingAppointments {

    private String phone;
    private int value;

    public BookingAppointments() {
    }

    public BookingAppointments(int value, String phone) {
        this.value = value;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
