package com.ja.miproyecto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Doctors extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Button add_doc;

    private RecyclerView rv;
    private AppointmentsAdapter adapter ;
    private FirebaseUser user;
    private ArrayList<RetrieveAppointments> appointments;
    private ArrayList<AppointmentNotif> appointment_notifs;
    private ProgressBar progressBar;
    private DatabaseReference reference_doc,patient;
    private int flag;
    private String encodedemail;
    private EditText search;
    private Toast backToast;
    private long backPressedTime;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DatabaseReference reference, reference_doctor;
    private ArrayList<AppointmentNotif> current_appt;
    private String email;
    private Date d1, d2;
    String website = "https://github.com/joaquinantonio";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        search= findViewById(R.id.editTextSearch_appointment);
        add_doc=findViewById(R.id.add_doctor);
        appointment_notifs=new ArrayList<>();
        appointments=new ArrayList<>();

        DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(this);
        email = doctors_session_management.getDoctorSession()[0].replace(".",",");

        rv=(RecyclerView)findViewById(R.id.recycler_available_appointments);
        final DatabaseReference nm= FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Chosen_Slots");
        patient= FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Patient_Chosen_Slots");
        reference_doctor = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Data");
        reference_doctor.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    startActivity(new Intent(Doctors.this, DoctorsUpdateProfile.class));
                    Toast.makeText(Doctors.this, "Please Update your Profile First", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        current_appt = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Appointments");
        String[] monthName = {"Jan", "Feb",
                "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov",
                "Dec"};
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        String month = monthName[cal.get(Calendar.MONTH)];
        int year = cal.get(Calendar.YEAR);
        String value = day + " " + month + " " + year;
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            d1 = sdformat.parse(day+"-"+month+"-"+year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reference.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    current_appt = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String value_2 = dataSnapshot.getKey();
                        value_2 = value_2.replace(" ", "-");
                        try {
                            d2 = sdformat.parse(value_2);
                            if (d1.compareTo(d2) <= 0) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    AppointmentNotif appointment_notif = dataSnapshot1.getValue(AppointmentNotif.class);
                                    if(appointment_notif.getAppointment_text().equals("1")) {
                                        current_appt.add(appointment_notif);
                                    }
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    adapter = new AppointmentsAdapter(current_appt, Doctors.this);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


    private void filter(String text) {
        ArrayList<AppointmentNotif> filterdNames = new ArrayList<>();
        int counter=0;
        for(AppointmentNotif obj: current_appt){
            counter=counter+1;
            if(obj.getDate().toLowerCase().contains(text.toLowerCase())){
                filterdNames.add(obj);
            }
        }
        adapter.filterList(filterdNames);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            startActivity(new Intent(this, AskRole.class));
        } else if (itemId == R.id.profile) {
            startActivity(new Intent(this, DoctorsViewProfile.class));
        } else if (itemId == R.id.slots) {
            startActivity(new Intent(this, DoctorChooseSlots.class));
        } else if (itemId == R.id.appointment) {
            startActivity(new Intent(this, Appointments.class));
        } else if (itemId == R.id.chats) {
            startActivity(new Intent(Doctors.this, DoctorChatDisplay.class));
        } else if (itemId == R.id.settingsApp) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        } else if (itemId == R.id.website) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(website)));
        } else if (itemId == R.id.logout) {
            DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(Doctors.this);
            doctors_session_management.removeSession();
            FirebaseAuth.getInstance().signOut();
            Intent intent1 = new Intent(Doctors.this, AskRole.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
                super.getOnBackPressedDispatcher().onBackPressed();
                ;
                return;
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }

}
