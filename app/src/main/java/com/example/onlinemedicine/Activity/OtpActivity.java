package com.example.onlinemedicine.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinemedicine.Apis.URLs;
import com.example.onlinemedicine.R;
import com.example.onlinemedicine.SignUp.SignUpActivity;
import com.example.onlinemedicine.VollySingletonClasses.VolleySingleton;
import com.example.onlinemedicine.usersession.UserSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class OtpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    UserSession session;
    private EditText codeText;
    private LinearLayout phoneLayout,codeLayout,wholelayout;
    private ProgressBar phoneBar,codeBar;
    private Button sendButton,verifyButton;
    private TextView phoneText;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String getnumber,getname,getemail,getpassword;
    // String url="https://kratipandeybpl.000webhostapp.com/FormFiller/user_registration.php";
    // String url=getString(R.string.base_url)+"FormFiller/user_registration.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Intent getdata=getIntent();
        getnumber=getdata.getExtras().getString("mobile");
        getemail=getdata.getExtras().getString("email");
        getname=getdata.getExtras().getString("name");
        getpassword=getdata.getExtras().getString("password");

        session=new UserSession(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        Log.e("onCreate: ",getString(R.string.base_url) );
        phoneText = (TextView)findViewById(R.id.phoneText);
        phoneText.setText(getnumber);

        codeText = (EditText)findViewById(R.id.codeText);

        phoneLayout = (LinearLayout) findViewById(R.id.phoneLayout);
        codeLayout = (LinearLayout) findViewById(R.id.codeLayout);
        wholelayout = (LinearLayout) findViewById(R.id.wholelayout);

        phoneBar = (ProgressBar)findViewById(R.id.phoneBar);
        codeBar = (ProgressBar)findViewById(R.id.codeBar);

        sendButton = (Button) findViewById(R.id.sendButton);
        verifyButton = (Button) findViewById(R.id.verifyButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneBar.setVisibility(View.VISIBLE);
                phoneText.setEnabled(false);
                sendButton.setEnabled(false);
                String phoneNumber = phoneText.getText().toString();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+phoneNumber,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        OtpActivity.this,
                        mCallBacks);

            }
        });

        //When you press verify code button

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                codeBar.setVisibility(View.VISIBLE);
                String code = codeText.getText().toString();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);

            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e("onVerificationFailed: ",e.toString() );
                Toast.makeText(OtpActivity.this,e.toString(),Toast.LENGTH_LONG).show();

            }


            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                phoneLayout.setVisibility(View.GONE);
                codeLayout.setVisibility(View.VISIBLE);
            }

        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = task.getResult().getUser();


                         register_user(getname,getemail,getpassword,getnumber);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI

                            Toast.makeText(OtpActivity.this,"error",Toast.LENGTH_LONG).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void register_user(String username, String email, String password, String phonenumber) {

        final KProgressHUD progressDialog=  KProgressHUD.create(OtpActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        try {
                            //converting response to json object
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("0")){


                                progressDialog.dismiss();
                                Toasty.error(getApplicationContext(),"Not Registered",Toast.LENGTH_SHORT,true).show();


                            }

                            if (status.equalsIgnoreCase("1")){

                                progressDialog.dismiss();
                                Toasty.success(getApplicationContext(),"registration successful",Toast.LENGTH_SHORT,true).show();
                                session.createLoginSession(" "," "," "," ");







                            }

                            if (status.equalsIgnoreCase("2")){


                                progressDialog.dismiss();
                                Toasty.error(getApplicationContext(),"User Already Exits",Toast.LENGTH_SHORT,true).show();



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
                params.put("user_name",username );
                params.put("contact",phonenumber );
                params.put("password", password);
                params.put("user_email", email);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}