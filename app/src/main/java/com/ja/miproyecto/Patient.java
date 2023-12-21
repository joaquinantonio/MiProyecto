package com.ja.miproyecto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class Patient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toast backToast;
    private long backPressedTime;
    private DrawerLayout drawerLayout1;
    String phoneNumber = "999";
    String website = "https://github.com/joaquinantonio/MiProyecto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        RecyclerView recyclerView_spec = findViewById(R.id.recycler_spec);
        ImageView all_doctors = findViewById(R.id.imageView_doc);
        SliderView sliderView = findViewById(R.id.slider);

        drawerLayout1 = findViewById(R.id.drawer_layout1);
        NavigationView navigationView1 = findViewById(R.id.nav_view1);
        Toolbar toolbar1 = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar1);

        navigationView1.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout1, toolbar1, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout1.addDrawerListener(toggle);
        toggle.syncState();
        navigationView1.setNavigationItemSelectedListener(Patient.this);
        navigationView1.setCheckedItem(R.id.nav_home);

        Integer [] specialisation_pic={R.drawable.infectious,R.drawable.dermavenereolepro,R.drawable.skin,R.drawable.diabetes,
                                        R.drawable.thyroid,R.drawable.hormone, R.drawable.immunology, R.drawable.rheuma, R.drawable.neuro, R.drawable.ophtha, R.drawable.cardiac, R.drawable.cancer,
                                        R.drawable.gastro, R.drawable.ent};

        String[] specialisation_type={"Infectious Disease","Dermatology & Venereology","Leprology","Endocrinology & Diabetes","Palliative Care","Hormone","Immunology","Rheumatology","Neurology","Ophthalmology","Cardiology ","Cancer Care / Oncology","Gastroenterology, Hepatology & Endoscopy","Ear Nose Throat"};

        ArrayList<MainSpecialisation> main_specialisations = new ArrayList<>();

        for(int i=0;i<specialisation_pic.length;i++){
           MainSpecialisation specialisation=new MainSpecialisation(specialisation_pic[i],specialisation_type[i]);
           main_specialisations.add(specialisation);

        }

        //design horizontal layout
        LinearLayoutManager layoutManager_spec=new LinearLayoutManager(
                Patient.this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView_spec.setLayoutManager(layoutManager_spec);
        recyclerView_spec.setItemAnimator(new DefaultItemAnimator());

        SpecialistAdapter specialist_adapter = new SpecialistAdapter(main_specialisations, Patient.this);
        recyclerView_spec.setAdapter(specialist_adapter);

        all_doctors.setOnClickListener(v -> {
            Intent intent=new Intent(Patient.this, AvailableDoctors.class);
            intent.putExtra("flag",0+"");
            startActivity(intent);
        });

        Integer [] sliderDataArrayList={R.drawable.image2,R.drawable.image1,R.drawable.image3};

        ArrayList<SliderData> slider_data = new ArrayList<>();

        for (Integer integer : sliderDataArrayList) {

            SliderData slider_data_arr = new SliderData(integer);
            slider_data.add(slider_data_arr);
        }

        SliderAdapter adapter = new SliderAdapter(this, slider_data);

        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        sliderView.setSliderAdapter(adapter);

        sliderView.setScrollTimeInSec(3);

        sliderView.setAutoCycle(true);

        sliderView.startAutoCycle();
    }

    private void moveToMainpage() {
        Intent intent=new Intent(Patient.this,AskRole.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void dialPhoneNumber() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.getOnBackPressedDispatcher().onBackPressed();
        if (drawerLayout1.isDrawerOpen(GravityCompat.START)) {
            drawerLayout1.closeDrawer(GravityCompat.START);
        }
        else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finishAffinity();
                backToast.cancel();
                super.getOnBackPressedDispatcher().onBackPressed();
                return;
            }
            else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            startActivity(new Intent(this, AskRole.class));
        } else if (itemId == R.id.doctors) {
            Intent intent = new Intent(Patient.this, AvailableDoctors.class);
            intent.putExtra("flag", 0 + "");
            startActivity(intent);
        } else if (itemId == R.id.appointments) { // error here
            startActivity(new Intent(Patient.this, PatientAppointments.class));
        } else if (itemId == R.id.chats) {
            startActivity(new Intent(Patient.this, PatientChatDisplay.class));
        } else if (itemId == R.id.settingsApp) {
            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent1.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));

            // Check if there is an activity that can handle the intent
            if (intent1.resolveActivity(getPackageManager()) != null) {
                startActivity(intent1);
            } else {
                // Handle the case where the intent cannot be resolved
                Toast.makeText(this, "Settings not available on this device", Toast.LENGTH_SHORT).show();
            }
        }
        else if (itemId == R.id.website) {
            // Assuming you want the same behavior for these cases
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(website)));
        } else if (itemId == R.id.logout) {
            PatientSessionManagement session_management = new PatientSessionManagement(Patient.this);
            session_management.removeSession();
            moveToMainpage();
        } else if (itemId == R.id.speciality_view_all) {
            startActivity(new Intent(Patient.this, ViewAllSpeciality.class));
        } else if (itemId == R.id.emergencycall) {
            dialPhoneNumber();
        }
        drawerLayout1.closeDrawer(GravityCompat.START);
        return true;
    }
}
