package com.example.onlinemedicine.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlinemedicine.R;
import com.example.onlinemedicine.usersession.Doctor_Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Doctor_Login extends AppCompatActivity {
       EditText doctorPassword,doctorEmail;
       Button doctorbtnLogin;
       Doctor_Session doctor_session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__login);
        doctorEmail=findViewById(R.id.inputEmail);
        doctorPassword=findViewById(R.id.inputPassword);
        doctorbtnLogin=findViewById(R.id.btnLogin);
        doctor_session=new Doctor_Session(this);
        doctorbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctorEmail.getText().toString().equalsIgnoreCase(""))
                {
                    doctorEmail.setError("Type Email Id");
                }
                else if(doctorPassword.getText().toString().equalsIgnoreCase("")) {
                    doctorPassword.setError("Type Password");
                }
                else
                {
                    Go_To_Login();
                }
            }
        });
    }

    private void Go_To_Login() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url="https://foldertechsoftware.com/online_medicine/doctor_login.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResponse: ",response );
                try {
                    JSONObject jsonObject=new JSONObject(response);
                  String status=jsonObject.getString("status");
                  if (status.equalsIgnoreCase("1"))
                  {
                      progressDialog.cancel();
                      doctor_session.createLoginSession(jsonObject.getString("name"),jsonObject.getString("doctor_email"),jsonObject.getString("doctor_contact"),jsonObject.getString("hospital"),jsonObject.getString("city"),jsonObject.getString("doctor_speciality"));
                      Intent go=new Intent(getApplicationContext(),Doctor_Dashbord.class);
                      startActivity(go);
                      finish();
                  }
                  else
                  {
                      progressDialog.cancel();
                      Toast.makeText(Doctor_Login.this, "Failed Try Agin..", Toast.LENGTH_SHORT).show();
                  }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>map=new HashMap<>();
                map.put("doctor_email",doctorEmail.getText().toString());
                map.put("password",doctorPassword.getText().toString());
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}