package com.example.onlinemedicine.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.onlinemedicine.R;
import com.example.onlinemedicine.SplashScreen.Doctor_Splash;
import com.example.onlinemedicine.SplashScreen.Splash;

public class Dashboard extends AppCompatActivity {
      CardView patient_card,doctor_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        patient_card=findViewById(R.id.patient_card);
        doctor_card=findViewById(R.id.doctor_card);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_dashboard);

        patient_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(getApplicationContext(), Splash.class);
                startActivity(go);
                finish();
            }
        });
        doctor_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(getApplicationContext(), Doctor_Splash.class);
                startActivity(go);
                finish();
            }
        });

    }
}