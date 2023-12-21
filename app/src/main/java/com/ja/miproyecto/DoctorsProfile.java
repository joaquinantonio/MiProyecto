package com.ja.miproyecto;

public class DoctorsProfile {
    private String name, type, aemail, desc, gender, experience, fees;
    private DoctorImages doc_pic, sign_pic;

    public DoctorsProfile(){

    }

    public DoctorsProfile(String name, String type, String aemail, String desc, String gender, String experience, DoctorImages doc_pic, DoctorImages sign_pic, String fees) {
        this.name = name;
        this.type = type;
        this.aemail = aemail;
        this.desc = desc;
        this.gender = gender;
        this.experience = experience;
        this.doc_pic = doc_pic;
        this.sign_pic = sign_pic;
        this.fees = fees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public DoctorImages getDoc_pic() {
        return doc_pic;
    }

    public void setDoc_pic(DoctorImages doc_pic) {
        this.doc_pic = doc_pic;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public DoctorImages getSign_pic() {
        return sign_pic;
    }

    public void setSign_pic(DoctorImages sign_pic) {
        this.sign_pic = sign_pic;
    }

    public String getAemail() {
        return aemail;
    }

    public void setAemail(String aemail) {
        this.aemail = aemail;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }
}
