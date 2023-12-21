package com.ja.miproyecto;

public class FeedbackModel {
    private float rating;
    private String feedback;

    public FeedbackModel(float rating, String feedback) {
        this.rating = rating;
        this.feedback = feedback;
    }

    public float getRating() {
        return rating;
    }

    public String getFeedback() {
        return feedback;
    }

}
