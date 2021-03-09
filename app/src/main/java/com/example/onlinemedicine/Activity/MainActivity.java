package com.example.onlinemedicine.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.onlinemedicine.Login.Login;
import com.example.onlinemedicine.Models.City_Model;
import com.example.onlinemedicine.Models.Hospital_Model;
import com.example.onlinemedicine.R;
import com.example.onlinemedicine.usersession.UserSession;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int currentPage = 0,NUM_PAGES=4;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;
    Toolbar toolbar;
    RecyclerView city_list,hospital_list;
    RecyclerView.LayoutManager layoutManager,layoutManager1;
    ArrayList<String>arrayList;
    ArrayList<City_Model>city_array_list;
    ArrayList<String>city_array_list_name;
    ArrayList<Hospital_Model>arrayList1;
    ViewPager viewPager2;
    Integer[] imageId = { R.drawable.medicine1,R.drawable.medicine2, R.drawable.medicine3, R.drawable.medicine5,R.drawable.medicine6};
     TextView select_city;
     AlertDialog alertDialog;
     ImageView online_medine;


     //
    UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar_nav);
        city_list=findViewById(R.id.city_list);
        hospital_list=findViewById(R.id.hospital_list);
        viewPager2=findViewById(R.id.view_pager);
        select_city=findViewById(R.id.select_city);
        online_medine=findViewById(R.id.online_medine);
        arrayList=new ArrayList<>();
        arrayList1=new ArrayList<>();
        city_array_list_name=new ArrayList<>();
        city_array_list=new ArrayList<>();



        session=new UserSession(getApplicationContext());


        session.checkLogin();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.currentusername);
        TextView useremailid = (TextView) headerView.findViewById(R.id.useremailid);
        //imageView = (ImageView) headerView.findViewById(R.id.imageView);
        // imageView.setImageDrawable(R.drawable.user);
        navigationView.setNavigationItemSelectedListener(this);
        PagerAdapter pageadapter = new CustomAdapter(MainActivity.this,imageId);
        viewPager2.setAdapter(pageadapter);


        navUsername.setText(session.getUserDetails().get("name"));
        useremailid.setText(session.getUserDetails().get("email"));





        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == imageId.length) {
                    currentPage = 0;
                }
                viewPager2.setCurrentItem(currentPage++, true);
            }
        };



        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
        layoutManager=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        city_list.setLayoutManager(layoutManager);
        Get_All_City();
        select_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence charSequence[] = city_array_list_name.toArray(new CharSequence[city_array_list_name.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.select_designation,null);
                builder.setCustomTitle(view1);
                builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.e( "onClick: ",String.valueOf(charSequence[i]));
                       // String select_month=String.valueOf(charSequence[i]);
                        select_city.setText(city_array_list.get(i).getCity_name());
                        //Log.e( "onClick: ",String.valueOf(i+1) );
                        Get_Hospital_name(city_array_list.get(i).getCity_id());



                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        arrayList.add("Allergist");
        arrayList.add("Andrologist");
        arrayList.add("Cardiologist");
        arrayList.add("Dermatologist");
        arrayList.add("Epidemiologist");
        arrayList.add("Gastroenterologist");
        arrayList.add("Hematologist");
        arrayList.add("Neurologist");
        CityAdapter cityAdapter=new CityAdapter(arrayList,getApplicationContext());
        city_list.setAdapter(cityAdapter);
       /* arrayList1.add("J.P Hospital");
        arrayList1.add("Hamidiya Hospital");
        arrayList1.add("LBS Hospital");
        arrayList1.add("AIIMS Hospital");
        arrayList1.add("Delhi Hospital");
        arrayList1.add("AIIMS Hospital");*/
        layoutManager1=new GridLayoutManager(this,2);
        hospital_list.setLayoutManager(layoutManager1);
        online_medine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(go);
            }
        });

    }

    private void Get_All_City() {
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
                      Get_Hospital_name(city_array_list.get(0).getCity_id());

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

    private void Get_Hospital_name(String city_name) {
        arrayList1.clear();
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
                            hospital_model.setHospital_id(jsonObject1.getString("hospital_id"));
                            arrayList1.add(hospital_model);
                            Hospitaladapter hospitaladapter=new Hospitaladapter(arrayList1,getApplicationContext());
                            hospital_list.setAdapter(hospitaladapter);
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
                HashMap<String,String>map=new HashMap<>();
                map.put("city_id",city_name);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void Get_Doctor_Type(String hospital_name) {
        String url="";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1"))
                    {
                         JSONArray jsonArray=jsonObject.getJSONArray("data");
                         for (int i=0;i<jsonArray.length();i++)
                         {
                             JSONObject jsonObject1=jsonArray.getJSONObject(i);
                             arrayList.add(jsonObject1.getString("doctor_type"));

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
                map.put("hospital_name",hospital_name);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            Intent go = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(go);
        } else if (id == R.id.nav_contact) {
            Toast.makeText(this, "Contact", Toast.LENGTH_SHORT).show();
            /*Intent go=new Intent(getApplicationContext(),Conatct.class);
            startActivity(go);*/

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
           // share();

        } else if (id == R.id.nav_logout) {

            session.logoutUser();
            Intent mySuperIntent = new Intent(MainActivity.this, Login.class);
            startActivity(mySuperIntent);
            finish();

        }
        return true;
    }
    class  CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>
    {
        public CityAdapter(ArrayList<String> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        ArrayList<String>arrayList;
        Context context;
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            View view=layoutInflater.inflate(R.layout.city_layout,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           holder.city_name.setText(arrayList.get(position));
          /* holder.city_layout.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent go=new Intent(getApplicationContext(),OrderActivity.class);
                   go.putExtra("city_name",arrayList.get(position));
                   startActivity(go);
               }
           });*/
           if (position>0)
           {
               //holder.city_image.setImageDrawable(R.drawable.circlebackgroundfour);
           }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
           TextView city_name;
           ImageView city_image;
           LinearLayout city_layout;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                city_name=itemView.findViewById(R.id.city_name);
                city_image=itemView.findViewById(R.id.city_image);
                city_layout=itemView.findViewById(R.id.city_layout);
            }
        }
    }
 class Hospitaladapter extends  RecyclerView.Adapter<Hospitaladapter.ViewHolder>
 {
     public Hospitaladapter(ArrayList<Hospital_Model> arrayList, Context context) {
         this.arrayList = arrayList;
         this.context = context;
     }

     ArrayList<Hospital_Model>arrayList;
     Context context;
     @NonNull
     @Override
     public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         LayoutInflater layoutInflater=LayoutInflater.from(context);
         View view=layoutInflater.inflate(R.layout.hospital_layout,parent,false);
         return new ViewHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
holder.hospital_name.setText(arrayList.get(position).getHospital_name());
     }

     @Override
     public int getItemCount() {
         Log.e("getItemCount: ", String.valueOf(arrayList.size()));
         return arrayList.size();
     }

     class ViewHolder extends RecyclerView.ViewHolder{
TextView hospital_name;
ImageView hospital_image;
         public ViewHolder(@NonNull View itemView) {
             super(itemView);
             hospital_name=itemView.findViewById(R.id.hospital_name);
             hospital_image=itemView.findViewById(R.id.hospital_image);
         }
     }
 }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}