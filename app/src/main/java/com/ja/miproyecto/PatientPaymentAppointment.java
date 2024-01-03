package com.ja.miproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PatientPaymentAppointment extends AppCompatActivity {

    private String check,date_val,fees,phone,email,chosen_time,question_data,pname,slot_val,fees_val;
    private TextView fees_show;
    private EditText tid;
    private DatabaseReference reference_user, reference_doctor, reference_booking, reference_patient, reference_details, reference_doctor_appt, reference_payment;
    private Button pay_app;
    private TextView paymentLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        reference_doctor = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Data");
        reference_doctor_appt = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Appointments");
        reference_booking = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Doctors_Chosen_Slots");
        reference_patient = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Patient_Chosen_Slots");
        reference_details = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Patient_Details");
        reference_payment = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Admin_Payment");
        PatientSessionManagement session = new PatientSessionManagement(PatientPaymentAppointment.this);
        phone = session.getSession();
        pname = getIntent().getSerializableExtra("pname").toString();
        email = getIntent().getSerializableExtra("email").toString();
        chosen_time = getIntent().getSerializableExtra("chosen_time").toString();
        question_data = getIntent().getSerializableExtra("question").toString();
        slot_val = getIntent().getSerializableExtra("slot_val").toString();
        date_val = getIntent().getSerializableExtra("date").toString();
        fees_val = getIntent().getSerializableExtra("fees").toString();
        check = getIntent().getSerializableExtra("check").toString();
        tid = (EditText) findViewById(R.id.paymentsinput);
        fees_show = (TextView) findViewById(R.id.fees);
        pay_app = (Button)findViewById(R.id.book_button);
        paymentLink = (TextView) findViewById(R.id.linkPayment);
        fees_show.setText("Please Pay RM "+ fees_val);

        pay_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String transactionid = tid.getText().toString().trim();
                if(transactionid.isEmpty()){
                    tid.setError("Reference ID is a required field");
                    tid.requestFocus();
                    return;
                }

                String finalSlot_val = slot_val;

                reference_booking.child(email).child(date_val).child(slot_val).child("Count").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            int count = snapshot.getValue(Integer.class);
                            count = count + 1;
                            BookingAppointments booking_appointments = new BookingAppointments(1, phone);
                            reference_booking.child(email).child(date_val).child(slot_val).child(check).setValue(booking_appointments);
                            reference_booking.child(email).child(date_val).child(finalSlot_val).child("Count").setValue(count);
                            PatientChosenTimeClass patient = new PatientChosenTimeClass(chosen_time, 0, 0);
                            reference_patient.child(phone).child(email).child(date_val).child(chosen_time).setValue(patient);
                            reference_doctor.child(email).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String dname = snapshot.getValue(String.class);
                                        AdminPaymentClass payment = new AdminPaymentClass(transactionid, dname, email, phone, pname, 0, date_val, chosen_time, 0);
                                        reference_payment.child("Payment0").child(phone).child(date_val).child(chosen_time).setValue(payment);
                                        Toast.makeText(PatientPaymentAppointment.this, "Payment Complete. Please Wait for Confirmation by Admin.", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(PatientPaymentAppointment.this, Patient.class));
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else{
                            Toast.makeText(PatientPaymentAppointment.this, "The Doctor is not available. Select other slot.", Toast.LENGTH_LONG).show(); // Select other slot with the same Email ID
                            Intent intent = new Intent(PatientPaymentAppointment.this, PatientBookingAppointments.class);
                            intent.putExtra("Email ID", email);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            return;
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                reference_doctor.child(email).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            String dname = snapshot.getValue(String.class);
//                            AdminPaymentClass payment = new AdminPaymentClass(transactionid, dname, email, phone, pname, 0, date_val, chosen_time, 0);
//                            reference_payment.child("Payment0").child(phone).child(date_val).child(chosen_time).setValue(payment);
//                            reference_booking.child(email).child(date_val).child(finalSlot_val).child("Count").addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    int count = snapshot.getValue(Integer.class);
//                                    count = count+1;
//                                    reference_booking.child(email).child(date_val).child(finalSlot_val).child("Count").setValue(count);
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//                            Toast.makeText(PatientPaymentAppointment.this, "Payment Done! Please Wait for Confirmation!", Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(PatientPaymentAppointment.this, Patient.class));
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BookingAppointments booking_appointments = new BookingAppointments(0, "null");
        reference_booking.child(email).child(date_val).child(slot_val).child(check).setValue(booking_appointments);
        startActivity(new Intent(PatientPaymentAppointment.this, Patient.class));
    }
}
