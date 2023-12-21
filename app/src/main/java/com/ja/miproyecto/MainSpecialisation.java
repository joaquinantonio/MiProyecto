package com.ja.miproyecto;

public class MainSpecialisation {

    private Integer specialisation_pic;
    private String specialisation_type;

    public MainSpecialisation(Integer specialisation_pic, String specialisation_type){

        this.specialisation_pic=specialisation_pic;
        this.specialisation_type=specialisation_type;
    }

    public Integer getSpecialisation_pic() {
        return specialisation_pic;
    }

    public String getSpecialisation_type() {
        return specialisation_type;
    }

}
