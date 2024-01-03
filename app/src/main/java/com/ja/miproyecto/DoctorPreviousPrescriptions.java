package com.ja.miproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class DoctorPreviousPrescriptions extends AppCompatActivity {
    private RecyclerView rv;
    private String[] gender;
    private String email,dname;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference prescription_doctor;
    private ArrayAdapter<String> gender_adapter;
    private ArrayList<GetPreviousPrescriptionDetails> presc;
    private DoctorPreviousPrescriptionAdapter adapter;

    String name,phone,date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_side_previous_prescriptions);

        rv=findViewById(R.id.recycler_previous_prescription);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        presc = new ArrayList<>();

        DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(this);
        email = doctors_session_management.getDoctorSession()[0].replace(".",",");
        name = getIntent().getStringExtra("pname");
        phone = getIntent().getStringExtra("phone");

        prescription_doctor = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Prescription_By_Doctor");
        prescription_doctor.child(email).child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                presc = new ArrayList<>();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        date=dataSnapshot.getKey();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            time=dataSnapshot1.getKey();
                            GetPreviousPrescriptionDetails gpd= new GetPreviousPrescriptionDetails(name,date,time,phone);
                            presc.add(gpd);

                        }


                    }
                    adapter = new DoctorPreviousPrescriptionAdapter(presc, DoctorPreviousPrescriptions.this);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}