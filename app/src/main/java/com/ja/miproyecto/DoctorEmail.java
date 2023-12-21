package com.ja.miproyecto;

public class DoctorEmail {
    private String emailid, type;
    public DoctorEmail(String emailid, String type) {
        this.emailid = emailid;
        this.type = type;
    }
    public String getEmailid() {
        return emailid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
