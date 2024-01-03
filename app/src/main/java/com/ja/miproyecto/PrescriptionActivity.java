package com.ja.miproyecto;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class PrescriptionActivity extends AppCompatActivity {

    private TextInputEditText name,age,address,height,weight,consultationType,labReport,diagnosis,relevant,exam,instruct ,rx;
    private EditText selectDate;
    private String finalDate;
    private DatabaseReference prescription_doctor;
    private DatePickerDialog.OnDateSetListener setListener;
    private ArrayAdapter<String> gender_adapter;
    private AutoCompleteTextView gender_view;
    private String gender_data;
    private String date,time,pname,phone,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        name = findViewById(R.id.textInputName);
        age = findViewById(R.id.textInputAge);
        address = findViewById(R.id.textInputBio);
        height = findViewById(R.id.textInputHeight);
        weight = findViewById(R.id.textInputWeight);
        consultationType = findViewById(R.id.textInputConsultationType);
        labReport = findViewById(R.id.textInputPreviousLabReport);
        diagnosis = findViewById(R.id.textInputDiagnosis);
        relevant = findViewById(R.id.textInputRelevantPoints);
        exam = findViewById(R.id.textInputExamination);
        instruct = findViewById(R.id.textInputInstruction);
        rx = findViewById(R.id.textInputRx);
        TextInputLayout gender_layout = findViewById(R.id.genderLayout_pres);
        gender_view = findViewById(R.id.genderTextView_pres);
        String[] gender = getResources().getStringArray(R.array.Gender);
        gender_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_gender, gender);
        gender_view.setAdapter(gender_adapter);
        gender_view.setThreshold(1);
        prescription_doctor = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Prescription_By_Doctor");

        DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(this);
        email = doctors_session_management.getDoctorSession()[0].replace(".",",");

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        selectDate = findViewById(R.id.consultationDateEdit);

        date= (String) getIntent().getSerializableExtra("date");
        time= (String) getIntent().getSerializableExtra("time");
        pname=(String) getIntent().getSerializableExtra("name");
        phone= (String) getIntent().getSerializableExtra("phone");
        name.setText(pname);

        selectDate.setText(date);

        gender_view.setOnItemClickListener((parent, view, position, id) -> gender_data = gender_adapter.getItem(position));

        prescription_doctor.child(email).child(phone).child(date).child(time).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    PrescriptionDetails prescriptionDetails = snapshot.getValue(PrescriptionDetails.class);
                    age.setText(prescriptionDetails.getAge());
                    gender_view.setText(prescriptionDetails.getPatientGender(), false);
                    address.setText(prescriptionDetails.getDoctorsAddress());
                    height.setText(prescriptionDetails.getPatientHeight());
                    weight.setText(prescriptionDetails.getPatientWeight());
                    consultationType.setText(prescriptionDetails.getConsultationType());
                    labReport.setText(prescriptionDetails.getPrevious_Lab_Report());
                    relevant.setText(prescriptionDetails.getRelevent_Point_from_history());
                    diagnosis.setText(prescriptionDetails.getDiagnosis_Information());
                    exam.setText(prescriptionDetails.getExaminations());
                    instruct.setText(prescriptionDetails.getInstructions());
                    rx.setText(prescriptionDetails.getRx());
                    selectDate.setText(prescriptionDetails.getConsultation_Date());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void send_prescription(View view) {

        String age_data = age.getText().toString().trim();
        String address_data = address.getText().toString().trim();
        String height_data = height.getText().toString().trim();
        String weight_data = weight.getText().toString().trim();
        String consultation_type_data = consultationType.getText().toString().trim();
        String previous_Lab_reports_data = labReport.getText().toString().trim();
        String relevent_point_data = relevant.getText().toString().trim();
        String diagnosis_data = diagnosis.getText().toString().trim();
        String exmination_data = exam.getText().toString().trim();
        String instructions_data = instruct.getText().toString().trim();
        String rx_data = Objects.requireNonNull(rx.getText()).toString().trim();
        String date_data = selectDate.getText().toString().trim();

        if(date_data.isEmpty()){
            selectDate.setText("date is required field");
            selectDate.requestFocus();
            return;
        }
        if(pname.isEmpty()){
            name.setError("Name is required Field");
            name.requestFocus();
            return;
        }
        if(age_data.isEmpty()){
            age.setError("Age is a required field");
            age.requestFocus();
            return;
        }

        PrescriptionDetails prescriptionDetails = new PrescriptionDetails(pname,gender_data, age_data, address_data,
                height_data, weight_data, consultation_type_data, date_data, previous_Lab_reports_data,
                relevent_point_data, diagnosis_data, exmination_data, instructions_data, rx_data,0);
        prescription_doctor.child(email).child(phone).child(date).child(time).setValue(prescriptionDetails);
        Toast.makeText(PrescriptionActivity.this, "Prescription Uploaded Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(PrescriptionActivity.this, Doctors.class));


    }
}