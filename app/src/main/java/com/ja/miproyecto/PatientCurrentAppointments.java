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

public class PatientCurrentAppointments extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private DatabaseReference reference;
    private ArrayList<AdminPaymentClass> current_appt;
    private String phone;
    private Date d1, d2;
    private PatientAppointmentAdapter adapter;
    private EditText search;

    public PatientCurrentAppointments(){

    }

    public static PatientCurrentAppointments getInstance() {
        return new PatientCurrentAppointments();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.row_current,container,false);
        PatientSessionManagement session = new PatientSessionManagement(getContext());
        phone = session.getSession();
        search=(EditText) view.findViewById(R.id.editTextSearch_current);
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
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        current_appt = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Admin_Payment");
        String[] monthName = {"Jan", "Feb",
                "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov",
                "Dec"};
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        String month = monthName[cal.get(Calendar.MONTH)];
        int year = cal.get(Calendar.YEAR);
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            d1 = sdformat.parse(day+"-"+month+"-"+year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reference.child("Payment1").child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                current_appt = new ArrayList<>();
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String value_2 = dataSnapshot.getKey();
                        value_2 = value_2.replace(" ", "-");
                        try {
                            d2 = sdformat.parse(value_2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (d1.compareTo(d2) <= 0) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                AdminPaymentClass payment = dataSnapshot1.getValue(AdminPaymentClass.class);
                                current_appt.add(payment);
                            }
                        }

                    }
                }
                reference.child("Payment0").child(phone).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String value_2 = dataSnapshot.getKey();
                                value_2 = value_2.replace(" ", "-");
                                try {
                                    d2 = sdformat.parse(value_2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (d1.compareTo(d2) <= 0) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        AdminPaymentClass payment = dataSnapshot1.getValue(AdminPaymentClass.class);
                                        current_appt.add(payment);
                                    }
                                }

                            }
                        }
                        if(current_appt.size() == 0){
                            Toast.makeText(getActivity(), "There are no Upcoming Appointments", Toast.LENGTH_SHORT).show();
                        }

                        adapter = new PatientAppointmentAdapter(current_appt);
                        recyclerView.setAdapter(adapter);

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
        return view;
    }

    private void filter(String text) {

        ArrayList<AdminPaymentClass> filterdNames = new ArrayList<>();
        for (AdminPaymentClass doc_data: current_appt) {
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
