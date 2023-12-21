package com.ja.miproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorsSidePatientDetails extends AppCompatActivity {

    private TextView Quest, name, phone_no, date_booked, time_booked;
    private String date, time, pname, Questions, phone,email;
    private String[] gender;
    private ArrayAdapter<String> gender_adapter;
    private DatabaseReference feedback;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_patient_details);

        date = (String) getIntent().getSerializableExtra("date");
        time = (String) getIntent().getSerializableExtra("time");
        pname = (String) getIntent().getSerializableExtra("name");
        Questions = (String) getIntent().getSerializableExtra("Questions");
        phone = (String) getIntent().getSerializableExtra("phone");
        feedback = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Feedback");
        Quest = findViewById(R.id.detaildesc_ques);
        name = findViewById(R.id.detailhead);
        date_booked = findViewById(R.id.detail_date);
        phone_no = findViewById(R.id.detailaddress);
        time_booked = findViewById(R.id.detail_time);
        btn = findViewById(R.id.show_feedback);
        btn.setVisibility(View.INVISIBLE);

        DoctorsSessionManagement doctors_session_mangement = new DoctorsSessionManagement(this);
        email = doctors_session_mangement.getDoctorSession()[0].replace(".",",");

        if (!Questions.equals("")) {
            Quest.setText(Questions);
        } else {
            Quest.setText("No Questions");
        }

        name.setText(pname);
        phone_no.setText(phone);
        date_booked.setText(date);
        time_booked.setText(time);

        feedback.child(email).child(phone).child(date).child(time).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    btn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        }

        public void upload_prescription (View view){

            Intent intent = new Intent(DoctorsSidePatientDetails.this, PrescriptionActivity.class);
            intent.putExtra("name", pname);
            intent.putExtra("date", date);
            intent.putExtra("time", time);
            intent.putExtra("phone", phone);


            startActivity(intent);

        }

        public void Previous_Prescriptions (View view){
            Intent intent = new Intent(DoctorsSidePatientDetails.this, DoctorPreviousPrescriptions.class);
            intent.putExtra("name", pname);
            intent.putExtra("phone", phone);
            startActivity(intent);
        }

        public void available_feedbacks (View view){
            Intent intent = new Intent(DoctorsSidePatientDetails.this, DoctorsShowFeedback.class);
            intent.putExtra("name", pname);
            intent.putExtra("phone", phone);
            intent.putExtra("date", date);
            intent.putExtra("time", time);
            startActivity(intent);
        }

    public void open_chat(View view) {
        Intent intent = new Intent(DoctorsSidePatientDetails.this, DoctorMessageActivity.class);
        intent.putExtra("phone",phone);
        startActivity(intent);
    }
    }