package com.example.onlinemedicine.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinemedicine.R;

import java.util.ArrayList;

public class Doctor_Dashbord extends AppCompatActivity {
  RecyclerView patient_list;
  RecyclerView.LayoutManager layoutManager;
  ArrayList<PatientModel>arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__dashbord);
        layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        patient_list=findViewById(R.id.patient_list);
        patient_list.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>();
        for (int i=0;i<10;i++)
        {
            PatientModel patientModel=new PatientModel();
            patientModel.setName("Suraj");
            patientModel.setEmail("daysuraj643@gmail.com");
            patientModel.setCity("Bhopal");
            arrayList.add(patientModel);
            Adapter adapter=new Adapter(arrayList,getApplicationContext());
            patient_list.setAdapter(adapter);

        }
    }

    public void go_to_back(View view) {
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
    {
        public Adapter(ArrayList<PatientModel> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        ArrayList<PatientModel>arrayList;
        Context context;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            View view=layoutInflater.inflate(R.layout.patient_layout,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}