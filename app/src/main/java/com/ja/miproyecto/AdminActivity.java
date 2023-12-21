package com.ja.miproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rv;
    private AdminAllDoctorAdapter adapter ;
    private Toast backToast;
    private long backPressedTime;
    private DrawerLayout drawerLayout;
    private List<DoctorsProfile> listData;
    private List<String> emaildata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_);
        EditText search = findViewById(R.id.editTextSearch_appointment);
        DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(this);
        String encodedemail = doctors_session_management.getDoctorSession()[0].replace(".", ",");
        rv=(RecyclerView)findViewById(R.id.recycler_doc);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData=new ArrayList<>();
        emaildata=new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        final DatabaseReference nm= FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Data");
        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listData=new ArrayList<>();
                emaildata=new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        String email=npsnapshot.getKey();
                        email = email.replace(",",".");
                        DoctorsProfile l=npsnapshot.getValue(DoctorsProfile.class);
                            listData.add(l);
                            emaildata.add(email);
                    }

                    adapter=new AdminAllDoctorAdapter(listData,emaildata, AdminActivity.this);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            startActivity(new Intent(this, AskRole.class));
        } else if (itemId == R.id.appointment_doc) {
            startActivity(new Intent(this, AdminAvailableAppointments.class));
        } else if (itemId == R.id.payment_doc) {
            startActivity(new Intent(this, AdminPayments.class));
        } else if (itemId == R.id.chat) {
            startActivity(new Intent(AdminActivity.this, AdminChatDisplay.class));
        } else if (itemId == R.id.Add_doc) {
            startActivity(new Intent(AdminActivity.this, AdminAddDoctor.class));
        } else if (itemId == R.id.feedback_doc) {
            startActivity(new Intent(AdminActivity.this, AdminFeedBack.class));
        } else if (itemId == R.id.settingsApp) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        } else if (itemId == R.id.website) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/joaquinantonio")));
        } else if (itemId == R.id.logout) {
            DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(AdminActivity.this);
            doctors_session_management.removeSession();
            FirebaseAuth.getInstance().signOut();
            Intent intent1 = new Intent(AdminActivity.this, AskRole.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public  void  filter(String text){

        ArrayList<DoctorsProfile> filterdNames = new ArrayList<>();
        for (DoctorsProfile doc_data: listData) {
            //if the existing elements contains the search input
            if (doc_data.getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(doc_data);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finishAffinity();
                backToast.cancel();
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }
}
