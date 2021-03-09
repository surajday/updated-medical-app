package com.example.onlinemedicine.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlinemedicine.Apis.URLs;
import com.example.onlinemedicine.Models.Hospital_Model;
import com.example.onlinemedicine.Models.Model;
import com.example.onlinemedicine.R;
import com.example.onlinemedicine.usersession.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CheakOut extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Model>arrayList;
    UserSession session;
    HashMap<String, String> user;
    String userEmail;
    Button gotopay;
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheak_out);


        //code for toolbar setup

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("6 Items in Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),OrderActivity.class));
            }
        });




        gotopay=findViewById(R.id.gotopay);
        lottieAnimationView=findViewById(R.id.noItems);
        recyclerView=findViewById(R.id.selected_item_list);
        layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>();


        //getting userdata from session

        session=new UserSession(getApplicationContext());
        user = new HashMap<>();
        user=session.getUserDetails();

         userEmail=user.get("email");


        //function for gettting cartlist from server
        GetCartList(userEmail);

    }






    public void GetCartList(String useremail) {


        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.GET_CART_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResponse: ",response );
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1"))
                    {
                        progressDialog.cancel();
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            Model model=new Model(jsonObject1.getString("name")
                            ,jsonObject1.getString("discount_price")
                            ,jsonObject1.getString("price")
                            ,jsonObject1.getString("id"));



                            arrayList.add(model);


                        }
                        OrderAdapter medicineAdapter=new OrderAdapter(getApplicationContext(),arrayList);
                        recyclerView.setAdapter(medicineAdapter);

                    }
                    else
                    {
                       recyclerView.setVisibility(View.GONE);
                       gotopay.setVisibility(View.GONE);
                       lottieAnimationView.setVisibility(View.VISIBLE);
                        progressDialog.cancel();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.cancel();
                    Toasty.error(getApplicationContext(),"Something Error", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toasty.error(getApplicationContext(),"Something Error", Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("user_id",useremail);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }













    public void go_to_back(View view) {
        Intent back=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(back);
        finish();
    }

    public void go_to_pay(View view) {
        Intent back=new Intent(getApplicationContext(), OrderDetail.class);
        startActivity(back);
        finish();
    }

    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>
    {
        Context context;

        public OrderAdapter(Context context, ArrayList<Model> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        ArrayList<Model>arrayList;
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            View view=layoutInflater.inflate(R.layout.select_item_list,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.medicine_name.setText(arrayList.get(position).getName());
            holder.price.setText(arrayList.get(position).getPrice());
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.cart_DiscPrice.setText(arrayList.get(position).getDiscprice());

            holder.delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   DeleteCartItem(userEmail,arrayList.get(position).getMedicineid());
                   arrayList.remove(position);
                    notifyItemRemoved(position);

                }
            });




        }

        private void DeleteCartItem(String useremail,String medicineid) {


            StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.DELETE_CART_SINGLE_ITEM, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("onResponse: ",response );
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1"))
                        {
                            Toasty.success(getApplicationContext(),"1 item Deleted", Toast.LENGTH_SHORT).show();

                            notifyDataSetChanged();


                        }
                        else
                        {
                            Toasty.error(getApplicationContext(),"Response Error", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toasty.error(getApplicationContext(),"Something Error", Toast.LENGTH_SHORT).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toasty.error(getApplicationContext(),"Something Error", Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> map=new HashMap<>();
                    map.put("user_id",useremail);
                    map.put("id",medicineid);
                    return map;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);




        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView medicine_name,price,cart_DiscPrice;
            ImageView delete_item;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                medicine_name=itemView.findViewById(R.id.cart_prtitle);
                price=itemView.findViewById(R.id.cart_prprice);
                delete_item=itemView.findViewById(R.id.deletecard);
                cart_DiscPrice=itemView.findViewById(R.id.cart_DiscPrice);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent back=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(back);
        finish();
    }


}