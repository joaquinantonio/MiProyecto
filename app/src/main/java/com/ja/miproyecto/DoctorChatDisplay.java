package com.ja.miproyecto;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DoctorChatDisplay extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DoctorUserAdapter doctorUserAdapter;
    private List<PatientDetails> mUsers;
    private DatabaseReference reference, reference_patient;
    private List<String> usersList, status_val;
    private FirebaseUser fuser;
    private String email;
    private HashSet<PatientDetails> hashSet;
    private ArrayList<String> phone_num;
    private TextView chatview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_display_chats);
        chatview = (TextView) findViewById(R.id.textChat);
        recyclerView = findViewById(R.id.recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(this);
        email = doctors_session_management.getDoctorSession()[0].replace(".", ",");
        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                int unread = 0;
                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Chat chat = snapshot1.getValue(Chat.class);

                        if (chat.getSender().equals(email)) {
                            usersList.add(chat.getReceiver());
                        }
                        if (chat.getReceiver().equals(email)) {
                            usersList.add(chat.getSender());
                        }
                        if (chat.getReceiver().equals(email) && !chat.isSeen()) {
                            unread++;
                        }
                    }
                    if (unread != 0) {

                        chatview.setText("Your Chats (" + unread + ")");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mUsers = new ArrayList<>();
        phone_num = new ArrayList<>();
        status_val = new ArrayList<>();
        reference_patient = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Patient_Details");
        reference_patient.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                phone_num = new ArrayList<>();
                status_val = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    PatientDetails details = snapshot1.getValue(PatientDetails.class);
                    mUsers.add(details);
                    phone_num.add(details.getPhone());
                }

                reference_patient.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (String phone : phone_num) {
                            if (snapshot.child(phone).child("status").exists()) {
                                status_val.add(snapshot.child(phone).child("status").getValue(String.class));
                            }
                            else{
                                status_val.add("offline");
                            }
                        }

                        doctorUserAdapter = new DoctorUserAdapter(DoctorChatDisplay.this, mUsers, status_val, true);
                        recyclerView.setAdapter(doctorUserAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
