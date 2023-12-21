package com.ja.miproyecto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class DoctorsUpdateProfile extends AppCompatActivity {
    private String email;
    private DatabaseReference reference_user, reference_doctor;
    private EditText name_box, email_box, bio_box, experience_box, fees_box;
    private TextView name_dis, email_dis;
    private ArrayAdapter<String> gender_adapter, speciality_adapter;
    private AutoCompleteTextView gender_view, speciality_view;
    private String speciality_data,gender_data;
    private Button update;
    private StorageReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private String alternate_email,bio,name, experience,fees;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int IMAGE_PICK_CODE_1 = 2000;
    private ImageView uploadButton, myImage;
    private static final int PERMISSION_CODE = 1001;
    private Uri selectImageUri, selectImageUri1,resultUri;
    private int flag = 0;
    private DoctorImages doctor_images, sign_images;
    private ProgressBar progressBar;
    private int type = 0, type2 = 0, type3 = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_update_profile);
        progressBar= findViewById(R.id.progressbar_update_profile);
        name_box = findViewById(R.id.textInputName);
        email_box = findViewById(R.id.textInputEmail);
        bio_box = findViewById(R.id.textInputBio);
        experience_box = findViewById(R.id.textInputexperience);
        fees_box = findViewById(R.id.textInputConsultationFee);
        name_dis = findViewById(R.id.displayName);
        email_dis = findViewById(R.id.emailidName);
        update = findViewById(R.id.update_btn);
        uploadButton = findViewById(R.id.uploadButton);
        myImage = findViewById(R.id.profile_image);
        //signimage = findViewById(R.id.signImage1);

        DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(this);
        email = doctors_session_management.getDoctorSession()[0].replace(".",",");

        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseStorage.getReference().child(email);
        reference_doctor = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Data");
        reference_user = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        TextInputLayout gender_layout = findViewById(R.id.ageLayout);
        gender_view = findViewById(R.id.desTextView);
        String[] gender = getResources().getStringArray(R.array.Gender);
        gender_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_gender, gender);
        gender_view.setAdapter(gender_adapter);
        gender_view.setThreshold(1);
        TextInputLayout speciality_layout = findViewById(R.id.descriptionLayout);
        speciality_view = findViewById(R.id.descriptionTextView);
        String[] speciality = getResources().getStringArray(R.array.Speciality);
        speciality_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_gender, speciality);
        speciality_view.setAdapter(speciality_adapter);
        speciality_view.setThreshold(1);
        speciality_data= "";

        reference_user.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name_dis.setText(snapshot.child("name").getValue(String.class));
                    email_dis.setText(snapshot.child("email").getValue(String.class));
                    name_box.setText(snapshot.child("name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference_doctor.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()){
                    email_box.setText(datasnapshot.child("email").getValue(String.class));
                    bio_box.setText(datasnapshot.child("desc").getValue(String.class));
                    experience_box.setText(datasnapshot.child("experience").getValue(String.class));
                    fees_box.setText(datasnapshot.child("fees").getValue(String.class));
                    doctor_images = datasnapshot.child("doc_pic").getValue(DoctorImages.class);
                    //sign_images = datasnapshot.child("sign_pic").getValue(DoctorImages.class);

                    if(doctor_images != null) {
                        Picasso.get().load(doctor_images.getUrl()).into(myImage);
                    }


                    String gender_v = datasnapshot.child("gender").getValue(String.class);
                    if (!(gender_v == null)) {
                        gender_view.setText(gender_v, false);
                        gender_data = gender_v;

                    }
                    String speciality_v = datasnapshot.child("type").getValue(String.class);
                    if (!(speciality_v == null)) {
                        speciality_view.setText(speciality_v, false);
                        speciality_data = speciality_v;
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        speciality_view.setOnItemClickListener((parent, view, position, id) -> speciality_data = speciality_adapter.getItem(position));
        gender_view.setOnItemClickListener((parent, view, position, id) -> gender_data = gender_adapter.getItem(position));
    }

    @Override
    protected void onResume() {
        super.onResume();

        uploadButton.setOnClickListener(v -> {
            type2 = 1;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                {
                    // permission not granted so we will request for that
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    // showing popup for runtime permission
                    requestPermissions(permissions,PERMISSION_CODE);
                }
                else
                {
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else
            {
                // system os is less than marshmallow
                pickImageFromGallery();
            }
        });

        update.setOnClickListener(v -> {
            name=name_box.getText().toString().trim();
            bio=bio_box.getText().toString().trim();
            alternate_email=email_box.getText().toString().trim();
            experience = experience_box.getText().toString().trim();
            fees = fees_box.getText().toString().trim();
            if(bio.isEmpty()){
                bio_box.setError("Bio is a required field");
                bio_box.requestFocus();
                return;
            }
            if(name.isEmpty()){
                name_box.setError("Name is a required field");
                name_box.requestFocus();
                return;
            }
            if(experience.isEmpty()){
                experience_box.setError("Experience is a required field");
                experience_box.requestFocus();
                return;
            }
            if(fees.isEmpty()){
                fees_box.setError("Consultation Fees is a required field");
                fees_box.requestFocus();
                return;
            }

            if (speciality_data.equals("")){
                Toast.makeText(DoctorsUpdateProfile.this, "Please Choose your Speciality!", Toast.LENGTH_SHORT).show();
                return;
            }

            DoctorsProfile data = new DoctorsProfile(name, speciality_data, alternate_email, bio, gender_data, experience,doctor_images,sign_images, fees);
            reference_doctor.child(email).setValue(data);
            reference_user.child(email).child("name").setValue(name);
            if (flag != 0) {
                upload_database();
            }
            Toast.makeText(DoctorsUpdateProfile.this, "Details Updated!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DoctorsUpdateProfile.this, Doctors.class));

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        name = name_box.getText().toString().trim();
        bio = bio_box.getText().toString().trim();
        alternate_email = email_box.getText().toString().trim();
        experience = experience_box.getText().toString().trim();
        fees = fees_box.getText().toString().trim();
        if (bio.isEmpty()) {
            bio_box.setError("Bio is a required field");
            bio_box.requestFocus();
        } else if (name.isEmpty()) {
            name_box.setError("Name is a required field");
            name_box.requestFocus();
        } else if (experience.isEmpty()) {
            experience_box.setError("Experience is a required field");
            experience_box.requestFocus();
        } else if (fees.isEmpty()) {
            fees_box.setError("Consultation Fees is a required field");
            fees_box.requestFocus();
        } else if (speciality_data.equals("")) {
            Toast.makeText(DoctorsUpdateProfile.this, "Please Choose your Speciality!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Click on Update to update profile!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == IMAGE_PICK_CODE  && resultCode == RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            selectImageUri = data.getData();
//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1, 1)
//                    .start(this);
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    resultUri = result.getUri();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectImageUri);
                myImage.setImageBitmap(bitmap);
                progressBar.setVisibility(View.INVISIBLE);
                flag = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if (requestCode == IMAGE_PICK_CODE_1  && resultCode == RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            selectImageUri1 = data.getData();
//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1, 1)
//                    .start(this);
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    resultUri = result.getUri();
            /*
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectImageUri1);
                signimage.setImageBitmap(bitmap);
                progressBar.setVisibility(View.INVISIBLE);
                flag = 2;
                signimage.setImageURI(selectImageUri1);
            } catch (IOException e) {
                e.printStackTrace();
            }
             */
        }
    }

    private void pickImageFromGallery(){
        if (type2 == 1 && type3 == 0){
            Toast.makeText(DoctorsUpdateProfile.this,"Upload Square Picture",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select the picture to upload"), IMAGE_PICK_CODE);
        }
        if (type3 == 1){
            Toast.makeText(DoctorsUpdateProfile.this,"Upload Square Picture",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select the picture to upload"), IMAGE_PICK_CODE_1);

        }
    }

    private void upload_database(){
        databaseReference.listAll().addOnSuccessListener(listResult -> {
            String fileName = "";
            if (type2 == 1) {
                fileName = "/Image 0";
                StorageReference databaseReference1 = firebaseStorage.getReference().child(email + fileName);
                databaseReference1.putFile(selectImageUri).addOnSuccessListener(taskSnapshot -> {
                    String name1 = taskSnapshot.getMetadata().getName();
                    final String[] url = new String[1];
                    databaseReference1.getDownloadUrl().addOnSuccessListener(uri -> {
                        url[0] = uri.toString();
                        doctor_images = new DoctorImages(name1, url[0]);
                        reference_doctor.child(email).child("doc_pic").setValue(doctor_images);
                    });
                });
            } if (type3 == 1) {
                fileName = "/Image 1";
                StorageReference databaseReference1 = firebaseStorage.getReference().child(email + fileName);
                databaseReference1.putFile(selectImageUri1).addOnSuccessListener(taskSnapshot -> {
                    String name1 = taskSnapshot.getMetadata().getName();
                    final String[] url = new String[1];
                    databaseReference1.getDownloadUrl().addOnSuccessListener(uri -> {
                        url[0] = uri.toString();
                        sign_images = new DoctorImages(name1, url[0]);
                        reference_doctor.child(email).child("sign_pic").setValue(sign_images);
                    });
                });
            }
        });
    }
}


