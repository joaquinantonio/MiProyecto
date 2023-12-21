package com.ja.miproyecto;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentStateAdapterAdmin extends FragmentStateAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private List<String> doctors = new ArrayList<>();

    public FragmentStateAdapterAdmin(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    @NonNull
    //@Override
    public CharSequence getPageTitle(int position) {
        return stringList.get(position);
    }

    public void addFragment(Fragment fragment, String title, String doc_name) {
        fragmentList.add(fragment);
        stringList.add(title);
        doctors.add(doc_name);
    }
}




/* If you don't have a specific need to keep track of doctor names within the adapter, you can simplify it by removing the doctors list:
This simplification assumes that you only need to associate each fragment with a title, and you don't need to track additional data like doctor names within the adapter.

Make sure to update your AdminAvailableAppointments class to reflect these changes when calling the addFragment method. If you need to associate a doctor's name with each fragment, you can handle that information within the AdminAvailableAppointments class.

* public class FragmentStateAdapterAdmin extends FragmentStateAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();

    public FragmentStateAdapterAdmin(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return stringList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        stringList.add(title);
    }
}
*
* */