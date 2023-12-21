package com.ja.miproyecto;

import android.content.Context;
import android.content.SharedPreferences;

public class DoctorsSessionManagement {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor_doc;
    private String SHARED_PREF_NAME="doctors_session";
    private String SESSION_KEY="doctors_session_user";
    private String SESSION_KEY_type="doctors_session";

    public DoctorsSessionManagement(Context context) {
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor_doc=sharedPreferences.edit();
    }

    public void saveDoctorSession(DoctorEmail doctor_email_id){
        String emailid_doc = doctor_email_id.getEmailid();
        String type = doctor_email_id.getType();
        editor_doc.putString(SESSION_KEY,emailid_doc);
        editor_doc.putString(SESSION_KEY_type,type);
        editor_doc.commit();
    }

    public String[] getDoctorSession(){
        String values[] = new String[2];
        values[0] = sharedPreferences.getString(SESSION_KEY, String.valueOf(-1));
        values[1] = sharedPreferences.getString(SESSION_KEY_type, String.valueOf(-1));
        return values;
    }

    public void removeSession(){
        editor_doc.putString(SESSION_KEY, String.valueOf(-1));
        editor_doc.putString(SESSION_KEY_type, String.valueOf(-1));
        editor_doc.commit();
    }
}