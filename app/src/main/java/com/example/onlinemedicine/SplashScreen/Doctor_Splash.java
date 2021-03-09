package com.example.onlinemedicine.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


import com.example.onlinemedicine.Activity.Doctor_Dashbord;
import com.example.onlinemedicine.Activity.Doctor_Login;
import com.example.onlinemedicine.R;
import com.example.onlinemedicine.usersession.Doctor_Session;

public class Doctor_Splash extends AppCompatActivity {
    int SPLASH_TIME=4000;

    Doctor_Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__splash);
        session=new Doctor_Session(getApplicationContext());
        Log.e("onCreate: ",String.valueOf(session.isLoggedIn()) );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session.isLoggedIn()) {
                    Log.e( "run: ", "True");
                    Intent mySuperIntent = new Intent(Doctor_Splash.this, Doctor_Dashbord.class);
                    startActivity(mySuperIntent);
                    finish();
                }
                else
                {
                    Log.e( "run: ", "false");

                    Intent mySuperIntent = new Intent(Doctor_Splash.this, Doctor_Login.class);
                    startActivity(mySuperIntent);
                    finish();
                }
                }



        }, SPLASH_TIME);
    }

}