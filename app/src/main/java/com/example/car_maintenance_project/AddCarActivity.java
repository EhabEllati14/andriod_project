package com.example.car_maintenance_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddCarActivity extends AppCompatActivity {

    private EditText licensePlateInput, makeInput, modelInput, yearInput;
    private Button addCarButton;
    private String customerId = ""; // Matches your database's `customer_id`
    DrawerLayout drawerLayout;
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
    String updatedAt = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        licensePlateInput = findViewById(R.id.license_plate_input);
        makeInput = findViewById(R.id.make_input);
        modelInput = findViewById(R.id.model_input);
        yearInput = findViewById(R.id.year_input);
        addCarButton = findViewById(R.id.add_car_button);
        drawerLayout = findViewById(R.id.drawer_layout);


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
        customerId = ssn;
        ImageView profile = findViewById(R.id.right_image);

        // Set click listener for Settings button
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(AddCarActivity.this, ProfileSetting.class);
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

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCarToDatabase();
            }
        });
    }

    private void addCarToDatabase() {
        String url = "http://"+IP+"/CAR_MAINTENANCE/add_car.php";

        String licensePlate = licensePlateInput.getText().toString().trim();
        String make = makeInput.getText().toString().trim();
        String model = modelInput.getText().toString().trim();
        String year = yearInput.getText().toString().trim();

        if (licensePlate.isEmpty() || make.isEmpty() || model.isEmpty() || year.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                Toast.makeText(AddCarActivity.this, "Car added successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddCarActivity.this, CustomerCars.class);
                                intent.putExtra("ssn", customerId);
                                startActivity(intent);                            } else {
                                Toast.makeText(AddCarActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddCarActivity.this, "Invalid response from server.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(AddCarActivity.this, "Error adding car: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add the parameters to be sent with the POST request
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", String.valueOf(customerId)); // Matches `customer_id` in your database
                params.put("license_plate", licensePlate);
                params.put("make", make);
                params.put("model", model);
                params.put("year", year);
                return params;
            }
        };

        queue.add(stringRequest);
    }
    public void homePage(View view){
        Intent intent = new Intent(AddCarActivity.this, CustomerDashboardActivity.class);
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
        Intent intent = new Intent(AddCarActivity.this, ViewTicketsActivity.class);
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
        Intent intent = new Intent(AddCarActivity.this, BookServiceActivity.class);
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

    public void showMoreCars(View view) {
        Intent intent = new Intent(AddCarActivity.this, CustomerCars.class);
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