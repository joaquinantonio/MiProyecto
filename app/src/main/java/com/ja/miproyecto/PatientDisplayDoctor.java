package com.ja.miproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class PatientDisplayDoctor extends AppCompatActivity {
    private Button book_app;
    private TextView doctor_name, doctor_spec, doctor_experience, doctor_fee, doctor_slots, doctor_about, emailid;
    private ImageView doctor_image;
    private DoctorImages doctor_images;
    private DatabaseReference reference_doctor, reference_booking;
    private int start, end;
    private Button prescription, chat_btn;
    private String encoded_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_doctor);
        doctor_name = (TextView) findViewById(R.id.doc_name_textview);
        doctor_spec = (TextView) findViewById(R.id.doc_spec_textview);
        doctor_experience = (TextView) findViewById(R.id.Exp_textview_val);
        doctor_fee = (TextView) findViewById(R.id.consult_charges_text_val);
        doctor_slots = (TextView) findViewById(R.id.Available_text_val);
        doctor_about = (TextView) findViewById(R.id.about_doctor);
        emailid = (TextView) findViewById(R.id.email_text_val);
        chat_btn = (Button) findViewById(R.id.chat_button);
        doctor_image = (ImageView) findViewById(R.id.imageView_doc_display);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);

        String date_val = DateFormat.format("dd MMM yyyy", startDate).toString();

        book_app = (Button) findViewById(R.id.book_button);
        String email = getIntent().getSerializableExtra("Email").toString();
        // String name = getIntent().getSerializableExtra("Doctors Name").toString();
        String name = "";

        if (getIntent().hasExtra("Doctors Name")) {
            Object doctorsNameObject = getIntent().getSerializableExtra("Doctors Name");
            if (doctorsNameObject != null) {
                name = doctorsNameObject.toString();
            }
        }

        String speciality = getIntent().getSerializableExtra("Speciality").toString();
        encoded_email = email.replace(".", ",");
        reference_doctor = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Data");
        reference_booking = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Chosen_Slots");
        emailid.setText("Email: " + email);
        DatabaseReference reference_details = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Patient_Details");
        reference_details.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    PatientSessionManagement session = new PatientSessionManagement(PatientDisplayDoctor.this);
                    String phone = session.getSession();

                    if (snapshot.child(encoded_email).exists()) {

                        if (snapshot.child(encoded_email).child(phone).exists()) {
                            chat_btn.setVisibility(View.VISIBLE);
                        } else {
                            chat_btn.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        chat_btn.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference_doctor.child(encoded_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    doctor_name.setText(datasnapshot.child("name").getValue(String.class));
                    doctor_spec.setText(datasnapshot.child("type").getValue(String.class));
                    doctor_about.setText(datasnapshot.child("desc").getValue(String.class));
                    doctor_experience.setText(datasnapshot.child("experience").getValue(String.class) + " years");
                    doctor_fee.setText("RM " + datasnapshot.child("fees").getValue(String.class));
                    doctor_images = datasnapshot.child("doc_pic").getValue(DoctorImages.class);
                    if (doctor_images != null) {
                        Picasso.get().load(doctor_images.getUrl()).into(doctor_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        doctor_slots.setText("");
        reference_booking.child(encoded_email).child(date_val).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String time = "";
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        start = Integer.parseInt(dataSnapshot.getKey().split(" - ", 5)[0]);
                        end = Integer.parseInt(dataSnapshot.getKey().split(" - ", 5)[1]);
                        time = time + String.format("%d:00 -%d:00", start, end) + "\n";

                    }
                    doctor_slots.setText(time);
                } else {
                    doctor_slots.setText("Doctor is not available today.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        book_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PatientDisplayDoctor.this, Calender.class);
                Intent intent = new Intent(PatientDisplayDoctor.this, PatientBookingAppointments.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });
    }

    public void current_prescription(View view) {
        Intent intent = new Intent(PatientDisplayDoctor.this, PatientSidePrescriptionRecycler.class);
        intent.putExtra("email", encoded_email);
        intent.putExtra("dr_name", doctor_name.getText().toString().trim());
        startActivity(intent);
    }

    public void open_chat(View view) {
        Intent intent = new Intent(PatientDisplayDoctor.this, PatientMessageActivity.class);
        intent.putExtra("email", encoded_email);
        startActivity(intent);
    }
}
/*
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class PatientDisplayDoctor extends AppCompatActivity {
    private Button book_app;
    private TextView doctor_name, doctor_spec, doctor_experience, doctor_fee, doctor_slots, doctor_about, emailid;
    private ImageView doctor_image;
    private DoctorImages doctor_images;
    private DatabaseReference reference_doctor, reference_booking;
    private int start, end;
    private Button prescription, chat_btn;
    private String encoded_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("IntentData", "Intent extras: " + getIntent().getExtras());
        setContentView(R.layout.activity_patient_view_doctor);
        doctor_name = (TextView) findViewById(R.id.doc_name_textview);
        doctor_spec = (TextView) findViewById(R.id.doc_spec_textview);
        doctor_experience = (TextView) findViewById(R.id.Exp_textview_val);
        doctor_fee = (TextView) findViewById(R.id.consult_charges_text_val);
        doctor_slots = (TextView) findViewById(R.id.Available_text_val);
        doctor_about = (TextView) findViewById(R.id.about_doctor);
        emailid = (TextView) findViewById(R.id.email_text_val);
        chat_btn = (Button) findViewById(R.id.chat_button);
        doctor_image = (ImageView) findViewById(R.id.imageView_doc_display);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);

        String date_val = DateFormat.format("dd MMM yyyy", startDate).toString();

        book_app = (Button) findViewById(R.id.book_button);
        Log.d("IntentData", "Intent extras: " + getIntent().getExtras());
        //String email = getIntent().getSerializableExtra("Email ID").toString();
        String email = String.valueOf(emailid);
        try {
            email = getIntent().getSerializableExtra("Email ID").toString();
            // ... proceed with code using email
        } catch (NullPointerException e) {
            Log.e("Error", "Email extra not found in intent");
            // Handle the error appropriately, e.g., display a message to the user
        }

        //String name = getIntent().getSerializableExtra("Doctors Name").toString();
        String.valueOf(doctor_name);
        try {
            getIntent().getSerializableExtra("Doctors Name").toString();
            // ... proceed with code using email
        } catch (NullPointerException e) {
            Log.e("Error", "Email extra not found in intent");
            // Handle the error appropriately, e.g., display a message to the user
        }

        // String speciality = getIntent().getSerializableExtra("Speciality").toString();
        String.valueOf(doctor_spec);
        try {
            getIntent().getSerializableExtra("Speciality").toString();
            // ... proceed with code using email
        } catch (NullPointerException e) {
            Log.e("Error", "Specialty extra not found in intent");
            // Handle the error appropriately, e.g., display a message to the user https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/
        }

        encoded_email = email.replace(".", ",");
        reference_doctor = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Data");
        reference_booking = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Chosen_Slots");
        emailid.setText("Email: " + email);
        DatabaseReference reference_details = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Patient_Details");
        reference_details.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    PatientSessionManagement session = new PatientSessionManagement(PatientDisplayDoctor.this);
                    String phone = session.getSession();
                    if (snapshot.child(encoded_email).exists()) {
                        if (snapshot.child(encoded_email).child(phone).exists()) {
                            chat_btn.setVisibility(View.VISIBLE);
                        }
                        else {
                            chat_btn.setVisibility(View.INVISIBLE);
                        }
                    }
                    else {
                        chat_btn.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try {
        reference_doctor.child(encoded_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    doctor_name.setText(datasnapshot.child("name").getValue(String.class));
                    doctor_spec.setText(datasnapshot.child("type").getValue(String.class));
                    doctor_about.setText(datasnapshot.child("desc").getValue(String.class));
                    doctor_experience.setText(datasnapshot.child("experience").getValue(String.class) + " years");
                    doctor_fee.setText("RM. " + datasnapshot.child("fees").getValue(String.class));
                    doctor_images = datasnapshot.child("doc_pic").getValue(DoctorImages.class);
                    if (doctor_images != null) {
                        Picasso.get().load(doctor_images.getUrl()).into(doctor_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }catch (DatabaseException e) {
            Log.e("DatabaseError", "Invalid path: " + e.getMessage());
            // Handle the error gracefully, e.g., display an error message to the user
        }

        doctor_slots.setText("");
        try {


        reference_booking.child(encoded_email).child(date_val).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String time = "";
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        start = Integer.parseInt(dataSnapshot.getKey().split(" - ", 5)[0]);
                        end = Integer.parseInt(dataSnapshot.getKey().split(" - ", 5)[1]);
                        time = time + String.format("%d:00 -%d:00", start, end) + "\n";

                    }
                    doctor_slots.setText(time);
                } else {
                    doctor_slots.setText("Doctor is not available today");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }catch (DatabaseException e) {
            Log.e("DatabaseError", "Invalid path: " + e.getMessage());
            // Handle the error gracefully, e.g., display an error message to the user
        }

        String finalEmail = email;
        book_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PatientDisplayDoctor.this, Calender.class);
                Intent intent = new Intent(PatientDisplayDoctor.this, PatientBookingAppointments.class);
                intent.putExtra("Email ID", finalEmail);
                startActivity(intent);
            }
        });
    }

    public void current_prescription(View view) {
        Intent intent = new Intent(PatientDisplayDoctor.this, PatientSidePrescriptionRecycler.class);
        intent.putExtra("email", encoded_email);
        intent.putExtra("dr_name", doctor_name.getText().toString().trim());
        startActivity(intent);
    }

    public void open_chat(View view) {
        Intent intent = new Intent(PatientDisplayDoctor.this, PatientMessageActivity.class);
        intent.putExtra("email", encoded_email);
        startActivity(intent);
    }
}
*/
