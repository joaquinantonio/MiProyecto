package com.ja.miproyecto;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class PatientAppointments extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);

        getTabs();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void getTabs() {
        final FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return null;
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                fragmentStateAdapter.addFragment(PatientPreviousAppointments.getInstance(),"Previous");
                fragmentStateAdapter.addFragment(PatientCurrentAppointments.getInstance(),"Upcoming");

                viewPager.setAdapter(fragmentStateAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }
}
