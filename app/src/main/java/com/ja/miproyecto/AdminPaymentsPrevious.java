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

import java.util.ArrayList;
import java.util.Date;

public class AdminPaymentsPrevious extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseUser user;
    private DatabaseReference reference;
    private ArrayList<AdminPaymentClass> previous_payment;
    private String email;
    private Date d1, d2;
    private AdminPaymentShowAdapter adapter;
    private EditText search;
    private Context mcontext;

    public AdminPaymentsPrevious() {

    }

    public static AdminPaymentsPrevious getInstance() {
        AdminPaymentsPrevious previousFragment = new AdminPaymentsPrevious();

        return previousFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.row_previous, container, false);

        search = (EditText) view.findViewById(R.id.editTextSearch_previous);
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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        previous_payment = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Admin_Payment");
        reference.child("Payment1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    previous_payment = new ArrayList<>();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                AdminPaymentClass payment_data = snapshot3.getValue(AdminPaymentClass.class);
                                previous_payment.add(payment_data);
                            }
                        }
                    }
                    adapter = new AdminPaymentShowAdapter(previous_payment);
                    recyclerView.setAdapter(adapter);
                } else {
                    if (mcontext != null) {
                        Toast.makeText(getActivity(), "There are no Completed Payments!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void filter(String text) {

        ArrayList<AdminPaymentClass> filterdNames = new ArrayList<>();
        for (AdminPaymentClass data : previous_payment) {
            //if the existing elements contains the search input
            if (data.getDate().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(data);
            }
        }
        adapter.filterList(filterdNames);
    }
}
