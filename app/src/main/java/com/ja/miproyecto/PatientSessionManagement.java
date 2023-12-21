package com.ja.miproyecto;

import android.content.Context;
import android.content.SharedPreferences;

public class PatientSessionManagement {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String SESSION_KEY="session_user";

    public PatientSessionManagement(Context context) {
        String SHARED_PREF_NAME = "session";
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public  void saveSession(PatientPhoneNumber patientPhoneNo){

        String phone_no= patientPhoneNo.getPhone_no();
        editor.putString(SESSION_KEY,phone_no).commit();

    }

    public String getSession(){

      return sharedPreferences.getString(SESSION_KEY, String.valueOf(-1));

    }

    public  void removeSession(){
        editor.putString(SESSION_KEY, String.valueOf(-1)).commit();
    }
}
