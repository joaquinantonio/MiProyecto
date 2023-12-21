package com.ja.miproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorsViewProfile extends AppCompatActivity {
    private TextView name,gender,email,speciality,bio,experience, fees;
    private ImageView sign;
    private CircleImageView circle_image;
    private DoctorImages doctor_images, sign_images;
    private ProgressBar progressBar;
    String years = " years.";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_profile);
        progressBar= findViewById(R.id.progressbar_view_profile);
        name = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        email = findViewById(R.id.email);
        experience = findViewById(R.id.experience);
        speciality = findViewById(R.id.speciality);
        bio = findViewById(R.id.bio);
        fees = findViewById(R.id.consultation);
        ImageView update = findViewById(R.id.imageView5);
        //sign = (ImageView) findViewById(R.id.signImage);
        circle_image = findViewById(R.id.profileImage);

        DoctorsSessionManagement doctors_session_mangement = new DoctorsSessionManagement(this);
        String email_id = doctors_session_mangement.getDoctorSession()[0].replace(".", ",");

        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference_doctor = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Data");
        DatabaseReference reference_user = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        reference_user.child(email_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name.setText(snapshot.child("name").getValue(String.class));
                    email.setText(snapshot.child("email").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference_doctor.child(email_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    gender.setText(datasnapshot.child("gender").getValue(String.class));
                    speciality.setText(datasnapshot.child("type").getValue(String.class));
                    bio.setText(datasnapshot.child("desc").getValue(String.class));
                    experience.setText(datasnapshot.child("experience").getValue(String.class)+ years);
                    fees.setText("RM " + datasnapshot.child("fees").getValue(String.class));
                    doctor_images = datasnapshot.child("doc_pic").getValue(DoctorImages.class);
                    sign_images = datasnapshot.child("sign_pic").getValue(DoctorImages.class);
                    if(doctor_images != null) {
                        Picasso.get().load(doctor_images.getUrl()).into(circle_image);
                    }
                    if(sign_images != null){
                        Picasso.get().load(sign_images.getUrl()).into(sign);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update.setOnClickListener(v -> startActivity(new Intent(DoctorsViewProfile.this, DoctorsUpdateProfile.class)));
    }
}
