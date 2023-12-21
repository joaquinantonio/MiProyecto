package com.ja.miproyecto;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminPayments extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_payment);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        Toast.makeText(AdminPayments.this, "Swipe to mark the payment", Toast.LENGTH_LONG).show();
        getTabs();
    }

    public void getTabs() {
        final FragmentStateAdapter adapter = new FragmentStateAdapter(this) { // Use 'this' as the FragmentActivity
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                // Return the appropriate fragment based on the position
                switch (position) {
                    case 0:
                        return com.ja.miproyecto.AdminPaymentsPrevious.getInstance();
                    case 1:
                        return AdminPaymentsCurrent.getInstance();
                    default:
                        return null;
                }
            }

            @Override
            public int getItemCount() {
                return 2; // Number of fragments
            }
        };

        viewPager.setAdapter(adapter);

        // Use TabLayoutMediator to link TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // You can customize tab labels here if needed
            switch (position) {
                case 0:
                    tab.setText("Completed");
                    break;
                case 1:
                    tab.setText("Upcoming");
                    break;
            }
        }).attach();
    }
}



/*
* package com.ja.miproyecto;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.FragmentStateAdapter;
import androidx.fragment.app.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminPayments extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_payment);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        Toast.makeText(AdminPayments.this, "Swipe to mark the payment!", Toast.LENGTH_LONG).show();
        getTabs();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getTabs() {
        final FragmentStateAdapter adapter = new FragmentStateAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                // Return the appropriate fragment based on the position
                switch (position) {
                    case 0:
                        return AdminPaymentsPrevious.getInstance();
                    case 1:
                        return AdminPaymentsCurrent.getInstance();
                    default:
                        return null;
                }
            }

            @Override
            public int getItemCount() {
                return 2; // Number of fragments
            }
        };

        new Handler().post(() -> {
            viewPager.setAdapter(adapter);

            // Use TabLayoutMediator to link TabLayout with ViewPager2
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                // You can customise tab labels here if needed
                switch (position) {
                    case 0:
                        tab.setText("Completed");
                        break;
                    case 1:
                        tab.setText("Upcoming");
                        break;
                }
            }).attach();
        });
    }
}

* */