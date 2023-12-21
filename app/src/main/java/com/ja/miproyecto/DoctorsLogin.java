package com.ja.miproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorsLogin extends AppCompatActivity implements View.OnClickListener {


    private TextView register, forgotPassword;
    private EditText editTextEmailMain, editTextPasswordMain;

    private Button signIn;
    private int flag = 0;
    private FirebaseAuth myAuth;
    private String encoded_email;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_login);

        signIn = (Button) findViewById(R.id.loginButton);
        signIn.setOnClickListener(this);

        editTextEmailMain = (EditText) findViewById(R.id.emailMain);
        editTextPasswordMain = (EditText) findViewById(R.id.passwordMain);
        progressBar = (ProgressBar) findViewById(R.id.progressbarlogin);

        myAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotPasswordText);
        forgotPassword.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            userLogin();
        } else if (v.getId() == R.id.forgotPasswordText) {
            startActivity(new Intent(this, ForgotPassword.class));
        }
    }


    private void userLogin() {
        String emailMain = editTextEmailMain.getText().toString().trim();
        String passwordMain = editTextPasswordMain.getText().toString().trim();
        encoded_email = EncodeString(emailMain);
        if (emailMain.isEmpty()) {
            editTextEmailMain.setError("Email is a required field !");
            editTextEmailMain.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailMain).matches()) {
            editTextEmailMain.setError("Please provide Valid Email !");
            editTextEmailMain.requestFocus();
            return;
        }
        if (passwordMain.isEmpty()) {
            editTextPasswordMain.setError("Password is a required field !");
            editTextPasswordMain.requestFocus();
            return;
        }
        if (passwordMain.length() < 6) {
            editTextPasswordMain.setError("Minimum length of password should be 6 characters !");
            editTextPasswordMain.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        myAuth.signInWithEmailAndPassword(emailMain, passwordMain).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {


                        databaseReference.child(encoded_email).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String usertype = dataSnapshot.child("user_type").getValue(String.class);
                                    if (usertype.equals("doctor")) {

                                        DoctorEmail doctor_email_id = new DoctorEmail(emailMain, "doctor");
                                        DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(DoctorsLogin.this);
                                        doctors_session_management.saveDoctorSession(doctor_email_id);
                                        startActivity(new Intent(DoctorsLogin.this, Doctors.class));
                                        progressBar.setVisibility(View.INVISIBLE);
                                    } else {
                                        DoctorEmail doctor_email_id = new DoctorEmail(emailMain, "Admin");
                                        DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(DoctorsLogin.this);
                                        doctors_session_management.saveDoctorSession(doctor_email_id);
                                        startActivity(new Intent(DoctorsLogin.this, AdminActivity.class));
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
//
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        progressBar.setVisibility(View.INVISIBLE);

                    } else if (flag == 0) {
                        showChangePasswordDialog();
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        user.sendEmailVerification();
                        progressBar.setVisibility(View.INVISIBLE);
//                        startActivity(new Intent(DoctorsLogin.this, ForgotPassword.class));
                        Toast.makeText(DoctorsLogin.this, "Check your Email to verify your account", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(DoctorsLogin.this, "Failed to Login ! Please check your credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void showChangePasswordDialog() {
        View view = LayoutInflater.from(DoctorsLogin.this).inflate(R.layout.update_password, null);
        final EditText passwordold = view.findViewById(R.id.passwordEt);
        final EditText passwordnew = view.findViewById(R.id.newpasswordEt);
        Button updatepass = view.findViewById(R.id.updatePasswordBtn);
        final AlertDialog.Builder builder = new AlertDialog.Builder(DoctorsLogin.this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        updatepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass = passwordold.getText().toString().trim();
                String newpass = passwordnew.getText().toString().trim();
                if (TextUtils.isEmpty(oldpass)) {
                    Toast.makeText(DoctorsLogin.this, "Enter Your Current Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newpass.length() < 6) {
                    Toast.makeText(DoctorsLogin.this, "Password length must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                flag = 1;
                updatePassword(oldpass, newpass);
            }
        });
    }

    private void updatePassword(String oldpass, String newpass) {
        FirebaseUser user = myAuth.getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldpass);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newpass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(DoctorsLogin.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DoctorsLogin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DoctorsLogin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

}
/*
package com.ja.miproyecto;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DoctorsLogin extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmailMain, editTextPasswordMain;
    private int flag = 0;
    private FirebaseAuth myAuth;
    private String encoded_email;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_login);

        Button signIn = (Button) findViewById(R.id.loginButton);
        signIn.setOnClickListener(this);

        editTextEmailMain = (EditText) findViewById(R.id.emailMain);
        editTextPasswordMain = (EditText) findViewById(R.id.passwordMain);
        progressBar = (ProgressBar) findViewById(R.id.progressbarlogin);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize Firebase Authentication
        myAuth = FirebaseAuth.getInstance();

        // Initialize App Check with SafetyNet provider
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance("https://mi-proyecto-8c7aa-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        TextView forgotPassword = (TextView) findViewById(R.id.forgotPasswordText);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.loginButton) {
            userLogin();
        } else if (viewId == R.id.forgotPasswordText) {
            startActivity(new Intent(this, ForgotPassword.class));
        }
    }

    private void userLogin() {
        String emailMain = editTextEmailMain.getText().toString().trim();
        String passwordMain = editTextPasswordMain.getText().toString().trim();
        encoded_email = EncodeString(emailMain);
        if (emailMain.isEmpty()) {
            editTextEmailMain.setError("Email is a required field ");
            editTextEmailMain.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailMain).matches()) {
            editTextEmailMain.setError("Please provide a valid email");
            editTextEmailMain.requestFocus();
            return;
        }
        if (passwordMain.isEmpty()) {
            editTextPasswordMain.setError("Password is a required field");
            editTextPasswordMain.requestFocus();
            return;
        }
        if (passwordMain.length() < 6) {
            editTextPasswordMain.setError("Minimum length of password should be 6 characters");
            editTextPasswordMain.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        myAuth.signInWithEmailAndPassword(emailMain, passwordMain).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                if (user.isEmailVerified()) {
                    databaseReference.child(encoded_email).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String usertype = dataSnapshot.child("user_type").getValue(String.class);
                                assert usertype != null;
                                DoctorEmail doctor_email_id;
                                DoctorsSessionManagement doctors_session_management = new DoctorsSessionManagement(DoctorsLogin.this);
                                if (Objects.equals(usertype, "doctor")) {
                                    doctor_email_id = new DoctorEmail(emailMain, "doctor");
                                    doctors_session_management.saveDoctorSession(doctor_email_id);
                                    startActivity(new Intent(DoctorsLogin.this, Doctors.class));
                                }
                                else {
                                    doctor_email_id = new DoctorEmail(emailMain, "Admin");
                                    doctors_session_management.saveDoctorSession(doctor_email_id);
                                    startActivity(new Intent(DoctorsLogin.this, AdminActivity.class));
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    progressBar.setVisibility(View.INVISIBLE);

                } else if (flag == 0) {
                    showChangePasswordDialog();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    user.sendEmailVerification();
                    progressBar.setVisibility(View.INVISIBLE);
//                        startActivity(new Intent(DoctorsLogin.this, ForgotPassword.class));
                    Toast.makeText(DoctorsLogin.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(DoctorsLogin.this, "Failed to Login. Please check your credentials", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showChangePasswordDialog() {
        View view = LayoutInflater.from(DoctorsLogin.this).inflate(R.layout.update_password, null);
        final EditText passwordold = view.findViewById(R.id.passwordEt);
        final EditText passwordnew = view.findViewById(R.id.newpasswordEt);
        Button updatepass = view.findViewById(R.id.updatePasswordBtn);
        final AlertDialog.Builder builder = new AlertDialog.Builder(DoctorsLogin.this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        updatepass.setOnClickListener(v -> {
            String oldpass = passwordold.getText().toString().trim();
            String newpass = passwordnew.getText().toString().trim();
            if (TextUtils.isEmpty(oldpass)) {
                Toast.makeText(DoctorsLogin.this, "Enter Your Current Password!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newpass.length() < 6) {
                Toast.makeText(DoctorsLogin.this, "Password Length must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.dismiss();
            flag = 1;
            updatePassword(oldpass, newpass);
        });
    }

    private void updatePassword(String oldpass, String newpass) {
        FirebaseUser user = myAuth.getCurrentUser();
        assert user != null;
        AuthCredential authCredential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), oldpass);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(aVoid -> user.updatePassword(newpass)
                        .addOnSuccessListener(aVoid1 -> Toast.makeText(DoctorsLogin.this, "Password Updated", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(DoctorsLogin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e -> Toast.makeText(DoctorsLogin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());

    }
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }
}
 */