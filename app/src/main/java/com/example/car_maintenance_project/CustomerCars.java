package com.example.car_maintenance_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Car;

public class CustomerCars extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ListView carsListView;
    private ArrayList<Car> carDetailsList;
    private ArrayAdapter<Car> adapter;
    private final String IP = "192.168.2.73";
    String ssn = "";
    String firstname = "";
    String lastname = "";
    String email = "";
    String password = "";
    String gender = "";
    String type = "";
    String address = "";
    String phonenumber = "";
    String dateofbirth = "";
    String createdAt = "";
    String updatedAt = "";    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_cars);

        drawerLayout = findViewById(R.id.drawer_layout);
        carsListView = findViewById(R.id.cars_list_view);
        carDetailsList = new ArrayList<>();

        ImageView navigationIcon = findViewById(R.id.left_image_1);
        navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        Intent intent = getIntent();
        ssn = intent.getStringExtra("ssn");
        firstname = intent.getStringExtra("first_name");
        lastname = intent.getStringExtra("last_name");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        gender = intent.getStringExtra("gender");
        type = intent.getStringExtra("type");
        address = intent.getStringExtra("address");
        phonenumber = intent.getStringExtra("phone_number");
        dateofbirth = intent.getStringExtra("date_of_birth");
        createdAt = intent.getStringExtra("created_at");
        updatedAt = intent.getStringExtra("updated_at");

        ImageView profile = findViewById(R.id.right_image);

        // Set click listener for Settings button
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(CustomerCars.this, ProfileSetting.class);
                settingsIntent.putExtra("ssn", ssn);
                settingsIntent.putExtra("first_name", firstname);
                settingsIntent.putExtra("last_name", lastname);
                settingsIntent.putExtra("email", email);
                settingsIntent.putExtra("gender", gender);
                settingsIntent.putExtra("address", address);
                settingsIntent.putExtra("phone_number", phonenumber);
                settingsIntent.putExtra("date_of_birth", dateofbirth);
                startActivity(settingsIntent);

            }
        });
        if (ssn != "") {
            fetchCarsData("http://"+IP+"/CAR_MAINTENANCE/get_customer_cars.php?userId=" + ssn);

        } else {
            Toast.makeText(this, "No user ID provided!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCarsData(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("cars")) {
                                // Extract the cars array from the response
                                JSONArray carsArray = response.getJSONArray("cars");
                                populateCarsList(carsArray);
                            } else if (response.has("error") && response.getBoolean("error")) {
                                // Display the error message if it exists
                                String message = response.getString("message");
                                Toast.makeText(CustomerCars.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CustomerCars.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerCars.this, "Error fetching cars: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }


//    private void fetchCarsData(String url) {
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        populateCarsList(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(CustomerCars.this, "Error fetching cars: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        queue.add(jsonArrayRequest);
//    }

    private void populateCarsList(JSONArray carsData) {
        carDetailsList.clear();
        for (int i = 0; i < carsData.length(); i++) {
            try {
                JSONObject car = carsData.getJSONObject(i);
                String licensePlate = car.getString("license_plate");
                String make = car.getString("make");
                String model = car.getString("model");
                String year = car.getString("year");
                Car carObj = new Car(licensePlate,make,model,year);
                carDetailsList.add(carObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                carDetailsList);
        carsListView.setAdapter(adapter);
    }
    public void homePage(View view){
        Intent intent = new Intent(CustomerCars.this, CustomerDashboardActivity.class);
        intent.putExtra("ssn", ssn);
        intent.putExtra("first_name", firstname);
        intent.putExtra("last_name", lastname);
        intent.putExtra("email", email);
        intent.putExtra("gender", gender);
        intent.putExtra("type", type);
        intent.putExtra("address", address);
        intent.putExtra("phone_number", phonenumber);
        intent.putExtra("date_of_birth", dateofbirth);
        intent.putExtra("created_at", createdAt);
        intent.putExtra("updated_at", updatedAt);
        startActivity(intent);
    }
    public void viewTicketsPage(View view){
        Intent intent = new Intent(CustomerCars.this, ViewTicketsActivity.class);
                intent.putExtra("ssn", ssn);
        intent.putExtra("first_name", firstname);
        intent.putExtra("last_name", lastname);
        intent.putExtra("email", email);
        intent.putExtra("gender", gender);
        intent.putExtra("type", type);
        intent.putExtra("address", address);
        intent.putExtra("phone_number", phonenumber);
        intent.putExtra("date_of_birth", dateofbirth);
        intent.putExtra("created_at", createdAt);
        intent.putExtra("updated_at", updatedAt);
        startActivity(intent);
    }
    public void bookServicePage(View view){
        Intent intent = new Intent(CustomerCars.this, BookServiceActivity.class);
        intent.putExtra("ssn", ssn);
        intent.putExtra("first_name", firstname);
        intent.putExtra("last_name", lastname);
        intent.putExtra("email", email);
        intent.putExtra("gender", gender);
        intent.putExtra("type", type);
        intent.putExtra("address", address);
        intent.putExtra("phone_number", phonenumber);
        intent.putExtra("date_of_birth", dateofbirth);
        intent.putExtra("created_at", createdAt);
        intent.putExtra("updated_at", updatedAt);        startActivity(intent);
    }
    public void addCar(View view) {
        Intent intent = new Intent(CustomerCars.this, AddCarActivity.class);
        intent.putExtra("ssn", ssn);
        intent.putExtra("first_name", firstname);
        intent.putExtra("last_name", lastname);
        intent.putExtra("email", email);
        intent.putExtra("gender", gender);
        intent.putExtra("type", type);
        intent.putExtra("address", address);
        intent.putExtra("phone_number", phonenumber);
        intent.putExtra("date_of_birth", dateofbirth);
        intent.putExtra("created_at", createdAt);
        intent.putExtra("updated_at", updatedAt);
        startActivity(intent);
    }

}