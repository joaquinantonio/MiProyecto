package com.ja.miproyecto;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import java.util.ArrayList;

public class AdminAvailableAppointments extends AppCompatActivity {
    private RecyclerView rv;
    private FirebaseUser user;
    private ProgressBar progressBar;
    private DatabaseReference reference_doc;
    private ArrayList<String> doc_names, doc_id;
    private ArrayAdapter<String> doctor_adapter;
    private TextInputLayout doctor_layout;
    private AutoCompleteTextView doctor_view;
    private String doctor_data, doctor_email;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_appointments);
        initView();
        initDoctorData();
    }

    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        doctor_layout = findViewById(R.id.docLayout);
        doctor_view = findViewById(R.id.docTextView);
    }

    private void initDoctorData() {
        doc_names = new ArrayList<>();
        doc_id = new ArrayList<>();
        reference_doc = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        reference_doc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doc_names.clear();
                doc_id.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (("doctor").equals(snapshot1.child("user_type").getValue(String.class))) {
                        doc_names.add(snapshot1.child("name").getValue(String.class));
                        doc_id.add(snapshot1.getKey());
                    }
                }
                doctor_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_gender, doc_names);
                doctor_view.setAdapter(doctor_adapter);
                doctor_view.setThreshold(1);
                doctor_data = "";
                doctor_email = "";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        doctor_view.setOnItemClickListener((parent, view, position, id) -> {
            doctor_data = doctor_adapter.getItem(position);
            doctor_email = doc_id.get(position);
            getTabs(doctor_data, doctor_email);
        });
    }

    private void getTabs(String name, String email) {
        final FragmentStateAdapterAdmin fragmentPagerAdapter = new FragmentStateAdapterAdmin(this);
        fragmentPagerAdapter.addFragment(AdminPreviousFragment.getInstance(doctor_email), "Previous", doctor_email);
        fragmentPagerAdapter.addFragment(AdminCurrentFragment.getInstance(doctor_email), "Current", doctor_email);

        viewPager.setAdapter(fragmentPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(fragmentPagerAdapter.getPageTitle(position))
        ).attach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Add any additional actions on resume if needed
    }
}