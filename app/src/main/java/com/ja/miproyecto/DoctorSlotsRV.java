package com.ja.miproyecto;

public class DoctorSlotsRV {

    private String date, time;
    private int slots_booked;
    private boolean showMenu = false;

    public DoctorSlotsRV(){

    }

    public DoctorSlotsRV(String date, String time, int slots_booked) {
        this.date = date;
        this.time = time;
        this.slots_booked = slots_booked;
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

    public int getSlots_booked() {
        return slots_booked;
    }

    public void setSlots_booked(int slots_booked) {
        this.slots_booked = slots_booked;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }
}
