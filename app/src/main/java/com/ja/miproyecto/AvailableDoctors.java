package com.ja.miproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AvailableDoctors extends AppCompatActivity {

    private List<DoctorsProfile> listData;
    private List<String> emaildata;

    private RecyclerView rv;
    private DoctorsAdapter adapter;
    private FirebaseUser user;

    private ProgressBar progressBar;
    private DatabaseReference reference_doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_doctors);

        EditText search = (EditText) findViewById(R.id.editTextSearch);

        progressBar = findViewById(R.id.progressbarpatient);
        progressBar.setVisibility(View.VISIBLE);
        rv = (RecyclerView) findViewById(R.id.recycler_available_doc);

        String speciality_type = getIntent().getStringExtra("Speciality_type");
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();
        emaildata = new ArrayList<>();
        String flag = getIntent().getStringExtra("flag");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        final DatabaseReference nm = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Data");
        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData = new ArrayList<>();
                emaildata = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        String email = npsnapshot.getKey();
                        email = email.replace(",", ".");
                        DoctorsProfile l = npsnapshot.getValue(DoctorsProfile.class);
                        if (flag.equals("1")) {
                            assert l != null;
                            if (speciality_type.equals(l.getType())) {
                                listData.add(l);
                                emaildata.add(email);
                            }
                        } else if (flag.equals("0")) {
                            listData.add(l);
                            emaildata.add(email);
                        }

                    }
                    if(listData.isEmpty()){
                        Toast.makeText(AvailableDoctors.this,"No Doctor is available",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    adapter = new DoctorsAdapter(listData, emaildata, AvailableDoctors.this);
                    rv.setAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void filter(String text) {

        ArrayList<DoctorsProfile> filterdNames = new ArrayList<>();
        for (DoctorsProfile doc_data : listData) {
            //if the existing elements contains the search input
            if (doc_data.getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(doc_data);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }
}