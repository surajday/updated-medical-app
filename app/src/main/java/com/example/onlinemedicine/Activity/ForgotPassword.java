package com.example.onlinemedicine.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinemedicine.Apis.URLs;
import com.example.onlinemedicine.Login.Login;
import com.example.onlinemedicine.R;
import com.example.onlinemedicine.SignUp.SignUpActivity;
import com.example.onlinemedicine.VollySingletonClasses.VolleySingleton;
import com.example.onlinemedicine.usersession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ForgotPassword extends AppCompatActivity {


    EditText UserEmail,UserPassword;
    String check,email,password;
    UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        UserEmail=findViewById(R.id.inputEmail);
        UserPassword=findViewById(R.id.inputPassword);

        session=new UserSession(getApplicationContext());


    }




    public void sendToCreateAccount(View view) {

        finish();
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
    }

    public void UpdatePassword(View view) {
        if(validateEmail() && validatePass()){


            email=UserEmail.getText().toString();
            password=UserPassword.getText().toString();
            LoginUser(email,password);

        }





    }

    private void LoginUser(String email, String password) {


        final KProgressHUD progressDialog=  KProgressHUD.create(ForgotPassword.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.FORGOT_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.e("tag",response);


                        try {
                            //converting response to json object
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("0")){


                                progressDialog.dismiss();
                                Toasty.error(getApplicationContext(),"Updating Failed", Toast.LENGTH_SHORT,true).show();

                                /*User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("username"),
                                        userJson.getString("email"),
                                        userJson.getString("gender")
                                );*/

                                //storing the user in shared preferences
                                //    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //     finish();


                            }

                            if (status.equalsIgnoreCase("1")){

                                progressDialog.dismiss();
                                Toasty.success(getApplicationContext(),"Passoword Updated Successfully",Toast.LENGTH_SHORT,true).show();
                                session.createLoginSession(jsonObject.getString("name"),jsonObject.getString("email"),jsonObject.getString("contact"),"");
                                finish();
                                startActivity(new Intent(getApplicationContext(),Login.class));

                            }



                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", email);
                params.put("password", password);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }

    private boolean validatePass() {


        check = UserPassword.getText().toString();

        if (check.length() < 4 || check.length() > 20) {
            return false;
        } else if (!check.matches("^[A-za-z0-9@]+")) {
            return false;
        }
        return true;
    }

    private boolean validateEmail() {

        check = UserEmail.getText().toString();

        if (check.length() < 4 || check.length() > 40) {
            return false;
        } else if (!check.matches("^[A-za-z0-9.@]+")) {
            return false;
        } else if (!check.contains("@") || !check.contains(".")) {
            return false;
        }

        return true;
    }

    public void sendToForgotPass(View view) {

        finish();
        startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
    }



   /* TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 40) {
                UserEmail.setError("Email Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9.@]+")) {
                UserEmail.setError("Only . and @ characters allowed");
            } else if (!check.contains("@") || !check.contains(".")) {
                UserEmail.setError("Enter Valid Email");
            }

        }

    };


    //TextWatcher for pass -----------------------------------------------------

    TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 20) {
                UserPassword.setError("Password Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9@]+")) {
                UserEmail.setError("Only @ special character allowed");
            }
        }

    };
*/
}