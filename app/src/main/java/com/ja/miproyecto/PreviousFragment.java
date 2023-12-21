package com.ja.miproyecto;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class PreviousFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private ArrayList<AppointmentNotif> previous_appt;
    private Date d1, d2;
    private Appointment_Show_Adapter adapter;

    public PreviousFragment(){

    }

    public static PreviousFragment getInstance() {
        return new PreviousFragment();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.row_previous,container,false);

        EditText search = view.findViewById(R.id.editTextSearch_previous);
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
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        previous_appt = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Appointments");
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        email = user.getEmail().replace(".", ",");
        DoctorsSessionManagement doctors_session_mangement = new DoctorsSessionManagement(getActivity());
        String email = doctors_session_mangement.getDoctorSession()[0].replace(".", ",");

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
                previous_appt = new ArrayList<>();
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String value_2 = dataSnapshot.getKey();
                        value_2 = value_2.replace(" ", "-");
                        try {
                            d2 = sdformat.parse(value_2);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (d1.compareTo(d2) > 0) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                AppointmentNotif appointment_notif = dataSnapshot1.getValue(AppointmentNotif.class);
                                previous_appt.add(appointment_notif);
                            }
                        }

                    }

                    adapter = new Appointment_Show_Adapter(previous_appt);
                    recyclerView.setAdapter(adapter);

                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "There are no previous appointments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
        }

    private void filter(String text) {

        ArrayList<AppointmentNotif> filterdNames = new ArrayList<>();
        for (AppointmentNotif doc_data: previous_appt) {
            //if the existing elements contains the search input
            if (doc_data.getDate().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(doc_data);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }
}
