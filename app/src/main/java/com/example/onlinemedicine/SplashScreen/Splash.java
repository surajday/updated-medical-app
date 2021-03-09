package com.example.onlinemedicine.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.onlinemedicine.Activity.MainActivity;
import com.example.onlinemedicine.Login.Login;
import com.example.onlinemedicine.R;
import com.example.onlinemedicine.usersession.UserSession;

public class Splash extends AppCompatActivity {
    int SPLASH_TIME=4000;

    UserSession session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session=new UserSession(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mySuperIntent = new Intent(Splash.this, MainActivity.class);
                startActivity(mySuperIntent);
                finish();

                //This 'finish()' is for exiting the app when back button pressed from Home page which is ActivityHome
                finish();




            }
        }, SPLASH_TIME);
    }

}