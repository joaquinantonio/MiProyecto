package com.ja.miproyecto;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Appointments extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;  // Use ViewPager2 instead of ViewPager

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
        final MyFragmentStateAdapter fragmentPagerAdapter = new MyFragmentStateAdapter(getSupportFragmentManager(), getLifecycle());

        fragmentPagerAdapter.addFragment(PreviousFragment.getInstance(),"Previous");
        fragmentPagerAdapter.addFragment(CurrentFragment.getInstance(),"Current");

        viewPager.setAdapter(fragmentPagerAdapter);

        // Use TabLayoutMediator for setting up tabs with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(fragmentPagerAdapter.getPageTitle(position))
        ).attach();
    }

}
