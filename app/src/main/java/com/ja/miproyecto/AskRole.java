package com.ja.miproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AskRole extends AppCompatActivity {

    private ImageView doctorButton,patientButton;
    private TextView doctorview, patientview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_role);
        doctorButton = findViewById(R.id.doctorLogin);
        patientButton = findViewById(R.id.patientLogin);
        doctorview = findViewById(R.id.doctortextView);
        patientview = findViewById(R.id.PatienttextView2);

        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDoctorSession();
            }
        });

        doctorview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDoctorSession();
            }
        });

        patientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPatientSession();
            }
        });

        patientview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPatientSession();
            }
        });
    }

    void checkPatientSession() {
        PatientSessionManagement session_management = new PatientSessionManagement(AskRole.this);
        String isUserLoggedin =session_management.getSession();
        if(!isUserLoggedin.equals("-1")){
            moveToPatientActivity();
        }
        else{
            Intent intent =new Intent(AskRole.this, PatientLogin.class);
            startActivity(intent);
        }
    }

    void checkDoctorSession() {

        DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(AskRole.this);
        String isDoctorLoggedin[] = doctors_session_management.getDoctorSession();
        if(!isDoctorLoggedin[0].equals("-1")){
            moveToDoctorActivity();
        }
        else {
            startActivity(new Intent(AskRole.this,DoctorsLogin.class));
        }
    }

    void moveToDoctorActivity() {
        DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(AskRole.this);
        String type[] = doctors_session_management.getDoctorSession();

        Intent intent;
        if(type[1].equals("Admin")){
            intent = new Intent(AskRole.this, AdminActivity.class);
        }
        //else if (type[1].equals("Admin")){
        else {
            intent = new Intent(AskRole.this, Doctors.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void moveToPatientActivity() {
        Intent intent = new Intent(AskRole.this, Patient.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}