package com.example.onlinemedicine.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlinemedicine.Apis.URLs;
import com.example.onlinemedicine.Models.City_Model;
import com.example.onlinemedicine.Models.Doctor_Model;
import com.example.onlinemedicine.Models.Hospital_Model;
import com.example.onlinemedicine.Models.Model;
import com.example.onlinemedicine.Models.Patient_Model;
import com.example.onlinemedicine.R;
import com.example.onlinemedicine.usersession.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;

public class OrderActivity extends AppCompatActivity {
    String city_name;
    RecyclerView medicine_list;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Model>arrayList;
    ArrayList<String>city_array_list_name;
    ArrayList<String>hosptalarrayList1_name;
    ArrayList<City_Model>city_array_list;
    ArrayList<Doctor_Model>doctorarrayList_id;
    ArrayList<Hospital_Model>hosptalarrayList1;
    ArrayList<Patient_Model>pasent_arraylist_id;
    ArrayList<String>doctorarrayList,pasent_arraylist;
    TextView select_city_name,select_hospital_name,select_doctor_name,select_pasent_name;
    AlertDialog alertDialog;
    LinearLayout medicine_layout,go_to_cheakout,order_layout;
    String city_id,hospital_id;


    private static final long LIMIT = 10000000000L;
    private static long last = 0;

    UserSession session;
    HashMap<String, String> user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        //code for toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_nav);
        toolbar.setTitle("Your Medicine list");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });











        medicine_list=findViewById(R.id.medicine_list);
        medicine_layout=findViewById(R.id.medicine_layout);
        order_layout=findViewById(R.id.order_layout);
        go_to_cheakout=findViewById(R.id.go_to_cheakout);
        layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        medicine_list.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>();
        doctorarrayList=new ArrayList<>();
        hosptalarrayList1=new ArrayList<>();
        pasent_arraylist=new ArrayList<>();
        pasent_arraylist_id=new ArrayList<>();
        city_array_list_name=new ArrayList<>();
        city_array_list=new ArrayList<>();
        hosptalarrayList1_name=new ArrayList<>();
        doctorarrayList_id=new ArrayList<>();


        session=new UserSession(getApplicationContext());
        user = new HashMap<>();
        user=session.getUserDetails();


      /*  hosptalarrayList1.add("J.P Hospital");
        hosptalarrayList1.add("Hamidiya Hospital");
        hosptalarrayList1.add("LBS Hospital");
        hosptalarrayList1.add("AIIMS Hospital");
        hosptalarrayList1.add("Delhi Hospital");*/
       /* pasent_arraylist.add("Manish Kumar");
        pasent_arraylist.add("Suraj Kumar");
        pasent_arraylist.add("Umesh Kumar");
        pasent_arraylist.add("Rahul Kumar");
        doctorarrayList.add("A.k Dubey");
        doctorarrayList.add("Dr.Prajapati");
        doctorarrayList.add("A.k Dubey");
        doctorarrayList.add("Dr.Prajapati");*/
        go_to_cheakout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick: ","cheak status" );
                Intent go=new Intent(getApplicationContext(),CheakOut.class);
                startActivity(go);
                finish();
            }
        });

            Model model=new Model("Combiflame","280","500","123");
            arrayList.add(model);

        Model model1=new Model("Paracetamol","100","150","123");
        arrayList.add(model1);

        Model model2=new Model("Pentop 40","150","210","123");
        arrayList.add(model2);

        Model model3=new Model("Petril md","120","200","123");
        arrayList.add(model3);

        Model model4=new Model("Telma 40","35","80","123");
        arrayList.add(model4);

            MedicineAdapter medicineAdapter=new MedicineAdapter(getApplicationContext(),arrayList);
            medicine_list.setAdapter(medicineAdapter);




        select_city_name=findViewById(R.id.city_name);
        select_hospital_name=findViewById(R.id.select_hospital);
        select_doctor_name=findViewById(R.id.select_doctor);
        select_pasent_name=findViewById(R.id.select_Patient);
        Get_All_City();
        select_city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence charSequence[] = city_array_list_name.toArray(new CharSequence[city_array_list_name.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.select_designation,null);
                builder.setCustomTitle(view1);
                builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.e( "onClick: ",String.valueOf(charSequence[i]));
                        // String select_month=String.valueOf(charSequence[i]);
                        select_city_name.setText(city_array_list.get(i).getCity_name());
                        //Log.e( "onClick: ",String.valueOf(i+1) );
                        city_id=city_array_list.get(i).getCity_id();
                        Get_Hospital_name(city_array_list.get(i).getCity_id());



                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
        select_hospital_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence charSequence[] = hosptalarrayList1_name.toArray(new CharSequence[hosptalarrayList1_name.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.select_designation,null);
                builder.setCustomTitle(view1);
                builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Log.e( "onClick: ",String.valueOf(charSequence[i]));
                        //  String select_month=String.valueOf(charSequence[i]);
                        select_hospital_name.setText(hosptalarrayList1.get(i).getHospital_name());
                        hospital_id=hosptalarrayList1.get(i).getHospital_id();

                        Get_Doctor_Type(hosptalarrayList1.get(i).getHospital_id());
                        // Log.e( "onClick: ",String.valueOf(i+1) );



                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        select_doctor_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence charSequence[] = doctorarrayList.toArray(new CharSequence[doctorarrayList.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.select_designation,null);
                builder.setCustomTitle(view1);
                builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.e( "onClick: ",String.valueOf(charSequence[i]));
                        String select_month=String.valueOf(charSequence[i]);
                        select_doctor_name.setText(doctorarrayList_id.get(i).getDoctor_name());
                        // Log.e( "onClick: ",String.valueOf(i+1) );
                        Log.e("onClick: ", doctorarrayList_id.get(i).getDoctor_id());
                        Get_Patient_List(doctorarrayList_id.get(i).getDoctor_id());



                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        select_pasent_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence charSequence[] = pasent_arraylist.toArray(new CharSequence[pasent_arraylist.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.select_designation,null);
                builder.setCustomTitle(view1);
                builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e( "onClick: ",String.valueOf(charSequence[i]));
                        String select_month=String.valueOf(charSequence[i]);
                        //select_pasent_name.setText(pasent_arraylist_id.get(i).getPatient_name());
                        Log.e( "onClick: ",String.valueOf(i+1) );
                        order_layout.setVisibility(View.GONE);
                        medicine_layout.setVisibility(View.VISIBLE);



                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private void Get_Patient_List(String doctor_id) {
        pasent_arraylist.clear();
        Log.e( "Id: ",hospital_id+doctor_id+city_id );
        String url="https://foldertechsoftware.com/online_medicine/get_patient.php";
        Log.e("Get_Patient_List: ", url);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e( "onResponse: ", response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            Patient_Model patient_model=new Patient_Model();
                            patient_model.setPatient_name(jsonObject1.getString("patient_name"));
                            patient_model.setPatient_id(jsonObject1.getString("patient_id"));
                            pasent_arraylist.add(jsonObject1.getString("patient_name"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                Log.e("getParams: ",doctor_id+","+ hospital_id+","+city_id);
                map.put("doctor_id",doctor_id);
                map.put("hospital_id",hospital_id);
                map.put("city_id",city_id);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }

    public Long generateRandomNumbers(){
        long id = System.currentTimeMillis() % LIMIT;
        if ( id <= last ) {
            id = (last + 1) % LIMIT;
        }
        return last = id;
    }

    private void Get_All_City() {
        city_array_list_name.clear();
        city_array_list.clear();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        String url="http://foldertechsoftware.com/online_medicine/get_city.php";
        Log.e( "Get_All_City: ",url );
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e( "onResponse: ",response );
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1"))
                    {
                        progressDialog.cancel();
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            City_Model city_model=new City_Model();
                            city_model.setCity_name(jsonObject1.getString("city_name"));
                            city_array_list_name.add(jsonObject1.getString("city_name"));
                            city_model.setCity_id(jsonObject1.getString("city_id"));
                            city_array_list.add(city_model);

                        }
                        //Get_Hospital_name(city_array_list.get(0).getCity_id());

                    }
                    else {
                        progressDialog.cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("onResponse: ", e.toString());
                    progressDialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Log.e("onErrorResponse: ",error.toString() );
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


    public void go_to_back(View view) {
        Intent back=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(back);
        finish();
    }

    public void go_to_cheak_out(View view) {
        Intent go=new Intent(getApplicationContext(),CheakOut.class);
        startActivity(go);
        finish();
    }

    class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder>{
        Context context;

        public MedicineAdapter(Context context, ArrayList<Model> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        ArrayList<Model>arrayList;
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            View view=layoutInflater.inflate(R.layout.cart_item_layout,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.medicine_name.setText(arrayList.get(position).getName());
            holder.price.setText(arrayList.get(position).getPrice());
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.cart_DiscPrice.setText(arrayList.get(position).getDiscprice());
            holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String userEmail=user.get("email");

                   /* Toast.makeText(context, arrayList.get(position).getName()+" "
                            +arrayList.get(position).getPrice()+" "
                            +arrayList.get(position).getDiscprice()+" "
                            +arrayList.get(position).getMedicineid(), Toast.LENGTH_LONG).show();*/


                    AddToCartMedicines(userEmail,arrayList.get(position).getName(),arrayList.get(position).getPrice(),arrayList.get(position)
                            .getDiscprice(),String.valueOf(generateRandomNumbers()));

                }
            });
        }

        //Adding Medicine To cart

        private void AddToCartMedicines(String Userid,String Medicinename,String Actualprice,String Discprice,String Medicineid) {

            StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.ADD_CART, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e( "onResponse: ", response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")){

                            Toasty.success(getApplicationContext(),"Added To Cart",Toast.LENGTH_SHORT,true).show();

                        }else{

                            Toasty.error(getApplicationContext(),"Error",Toast.LENGTH_SHORT,true).show();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                    map.put("user_id",Userid);
                    map.put("id",Medicineid);
                    map.put("name",Medicinename);
                    map.put("price",Actualprice);
                    map.put("discount_price",Discprice);

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
            ImageView add_to_cart;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                medicine_name=itemView.findViewById(R.id.cart_prtitle);
                price=itemView.findViewById(R.id.cart_prprice);
                add_to_cart=itemView.findViewById(R.id.addcard);
                cart_DiscPrice=itemView.findViewById(R.id.cart_DiscPrice);
            }
        }
    }

    private void Get_Hospital_name(String city_name) {
        hosptalarrayList1.clear();
        hosptalarrayList1_name.clear();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        String url="https://foldertechsoftware.com/online_medicine/get_hospital_name_by_city.php";
        Log.e( "Get_Hospital_name: ",url );
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
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            Hospital_Model hospital_model=new Hospital_Model();
                            hospital_model.setHospital_name(jsonObject1.getString("hospital_name"));
                            hosptalarrayList1_name.add(jsonObject1.getString("hospital_name"));
                            hospital_model.setHospital_id(jsonObject1.getString("hospital_id"));
                            hosptalarrayList1.add(hospital_model);

                        }
                        //  Get_Doctor_Type(arrayList1.get(0).getHospital_id());

                    }
                    else
                    {
                        progressDialog.cancel();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.cancel();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("city_id",city_name);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


    private void Get_Doctor_Type(String hospital_name) {
        Log.e( "Get_Doctor_Type: ",hospital_name );
        doctorarrayList.clear();
        String url="https://foldertechsoftware.com/online_medicine/get_doctor.php";
        Log.e( "Get_Doctor_Type: ",url );
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e( "onResponse: ",response );
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            Doctor_Model doctor_model=new Doctor_Model();
                            doctor_model.setDoctor_name(jsonObject1.getString("doctor_name"));
                            doctor_model.setDoctor_id(jsonObject1.getString("doctor_id"));
                            doctorarrayList.add(jsonObject1.getString("doctor_name"));
                            doctorarrayList_id.add(doctor_model);

                        }
                    }
                    else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
                Log.e( "getParams: ",hospital_name );
                map.put("hospital_id",hospital_name);
                map.put("city_id",city_id);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }


}