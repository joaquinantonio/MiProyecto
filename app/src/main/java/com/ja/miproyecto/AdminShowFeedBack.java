package com.ja.miproyecto;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminShowFeedBack extends AppCompatActivity {

    private TextView feedbackText;
    private EditText feedbackEdit;
    private RatingBar feedbackRating;
    private float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_show_feedback);
        feedbackEdit = findViewById(R.id.editFeedback);
        feedbackText = findViewById(R.id.feedback);
        feedbackRating = findViewById(R.id.ratingBar);
        String date = (String) getIntent().getSerializableExtra("date");
        String time = (String) getIntent().getSerializableExtra("time");
        String name = (String) getIntent().getSerializableExtra("name");
        String phone = (String) getIntent().getSerializableExtra("phone");
        String email = (String) getIntent().getSerializableExtra("email");
        DatabaseReference feedback = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Feedback");

        feedback.child(email).child(phone).child(date).child(time).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    FeedbackModel feedback_model = snapshot.getValue(FeedbackModel.class);
                    feedbackEdit.setText(feedback_model.getFeedback());
                    rating = feedback_model.getRating();
                    rating = Float.parseFloat(String.valueOf(rating));
                    feedbackRating.setStepSize((float) 0.5);
                    feedbackRating.setRating(rating);
                    feedbackRating.setFocusable(false);

                    if (rating == 0 || rating == 0.5) {
                        feedbackText.setText("Very Dissatisfied");
                    } else if (rating == 1 || rating == 1.5) {
                        feedbackText.setText("Dissatisfied");
                    } else if (rating == 2 || rating == 2.5) {
                        feedbackText.setText("Okay");
                    } else if (rating == 3) {
                        feedbackText.setText("Okay!");
                    } else if (rating == 3.5) {
                        feedbackText.setText("Satisfied");
                    } else if (rating == 4 || rating == 4.5) {
                        feedbackText.setText("Satisfied");
                    } else if (rating == 5) {
                        feedbackText.setText("Very Satisfied");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
