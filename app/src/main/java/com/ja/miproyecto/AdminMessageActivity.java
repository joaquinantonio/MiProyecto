package com.ja.miproyecto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminMessageActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView username;
    private DatabaseReference reference;
    private Intent intent;
    private ImageButton btn_send, btn_attach;
    private EditText text_send;
    private AdminMessageAdapter messageAdapter;
    private List<Chat> mchat;
    private RecyclerView recyclerView;
    private String userid, phone;
    private StorageReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri selectImageUri;
    private Intent intent_seen;
    private ValueEventListener seenListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_message_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        btn_attach = findViewById(R.id.btn_attach);
        btn_attach.setVisibility(View.GONE);
        btn_send.setVisibility(View.GONE);
        text_send = findViewById(R.id.text_send);
        text_send.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseStorage = FirebaseStorage.getInstance();
        userid = getIntent().getSerializableExtra("email").toString();
        databaseReference = firebaseStorage.getReference().child(userid);
        phone = getIntent().getSerializableExtra("phone").toString();
        reference = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                username.setText(phone);
                readMessage(userid, phone, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage(String myid, String uid, String imageurl) {
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if ((chat.getReceiver().equals(uid) && chat.getSender().equals(myid)) || (chat.getReceiver().equals(myid) && chat.getSender().equals(uid))) {
                        mchat.add(chat);
                    }
                }
                messageAdapter = new AdminMessageAdapter(AdminMessageActivity.this, mchat, null, userid);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}