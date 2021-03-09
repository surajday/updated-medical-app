package com.example.onlinemedicine.usersession;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.example.onlinemedicine.Activity.Dashboard;

import java.util.HashMap;

public class Doctor_Session {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "UserSessionPref";

    // First time logic Check
    public static final String FIRST_TIME = "firsttime";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Mobile number (make variable public to access from outside)
    public static final String KEY_MOBiLE = "mobile";

    // user avatar (make variable public to access from outside)
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_CITY_ID = "cityid";
    public static final String KEY_HOSPITAL_ID = "hospital_id";
    public static final String KEY_SPECIALITY = "speciality";

    public Doctor_Session(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void createLoginSession(String name, String email, String mobile, String hospital_id,String city_id,String speciality){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing phone number in pref
        editor.putString(KEY_MOBiLE, mobile);

        // Storing image url in pref
        editor.putString(KEY_HOSPITAL_ID, hospital_id);
        editor.putString(KEY_CITY_ID, city_id);
        editor.putString(KEY_SPECIALITY, speciality);

        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user phone number
        user.put(KEY_MOBiLE, pref.getString(KEY_MOBiLE, null));

        // user avatar
        user.put(KEY_HOSPITAL_ID, pref.getString(KEY_HOSPITAL_ID, null)) ;
        user.put(KEY_CITY_ID, pref.getString(KEY_CITY_ID, null)) ;
        user.put(KEY_SPECIALITY, pref.getString(KEY_SPECIALITY, null)) ;

        // return user
        return user;
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN,false);
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, Dashboard.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

}
