package com.example.car_maintenance_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookServiceActivity extends AppCompatActivity {

    private Spinner serviceSpinner, carSpinner, emergencySpinner;
    private Button submitButton;
    private DrawerLayout drawerLayout;
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
        setContentView(R.layout.activity_book_service);
        drawerLayout = findViewById(R.id.drawer_layout);
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
                Intent settingsIntent = new Intent(BookServiceActivity.this, ProfileSetting.class);
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

        // Initialize fields
        serviceSpinner = findViewById(R.id.service_spinner);
        carSpinner = findViewById(R.id.car_spinner);
        emergencySpinner = findViewById(R.id.emergency_spinner);
        submitButton = findViewById(R.id.submit_button);

        if (ssn == "") {
            Toast.makeText(this, "User ID is required", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Load services and cars
        loadServices();
        loadCars();

        // Set up emergency levels
        ArrayAdapter<String> emergencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Low", "Medium", "High"});
        emergencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emergencySpinner.setAdapter(emergencyAdapter);

        // Submit booking
        submitButton.setOnClickListener(v -> submitBooking());
    }

    private void loadServices() {
        String url = "http://" + IP + "/CAR_MAINTENANCE/get_services.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.has("services")) {
                            // Extract services array
                            JSONArray servicesArray = response.getJSONArray("services");
                            ArrayList<String> services = new ArrayList<>();

                            // Populate the services list
                            for (int i = 0; i < servicesArray.length(); i++) {
                                JSONObject service = servicesArray.getJSONObject(i);
                                services.add(service.getString("service_name"));
                            }

                            // Create and set the adapter for the spinner
                            ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    services
                            );
                            serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            serviceSpinner.setAdapter(serviceAdapter);

                        } else if (response.has("error") && response.getBoolean("error")) {
                            // Display error message if provided
                            String message = response.getString("message");
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing services", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching services: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(jsonObjectRequest);
    }

//    private void loadServices() {
//        String url = "http://"+IP+"/CAR_MAINTENANCE/get_services.php";
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
//                response -> {
//                    ArrayList<String> services = new ArrayList<>();
//                    try {
//                        for (int i = 0; i < response.length(); i++) {
//                            JSONObject service = response.getJSONObject(i);
//                            services.add(service.getString("service_name"));
//                        }
//                        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, services);
//                        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        serviceSpinner.setAdapter(serviceAdapter);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(this, "Error parsing services", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                error -> Toast.makeText(this, "Error fetching services", Toast.LENGTH_SHORT).show());
//        queue.add(jsonArrayRequest);
//    }

    private void loadCars() {
        String url = "http://" + IP + "/CAR_MAINTENANCE/get_license_plate.php?userId=" + ssn;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.has("license_plates")) {
                            // Extract license plates array
                            JSONArray carsArray = response.getJSONArray("license_plates");
                            ArrayList<String> cars = new ArrayList<>();

                            // Populate the cars list
                            for (int i = 0; i < carsArray.length(); i++) {
                                JSONObject car = carsArray.getJSONObject(i);
                                cars.add(car.getString("license_plate"));
                            }

                            // Create and set the adapter for the spinner
                            ArrayAdapter<String> carAdapter = new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    cars
                            );
                            carAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            carSpinner.setAdapter(carAdapter);

                        } else if (response.has("error") && response.getBoolean("error")) {
                            // Display error message if provided
                            String message = response.getString("message");
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing cars", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching cars: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(jsonObjectRequest);
    }


//    private void loadCars() {
//        String url = "http://"+IP+"/CAR_MAINTENANCE/get_license_plate.php?userId=" + ssn;
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
//                response -> {
//                    ArrayList<String> cars = new ArrayList<>();
//                    try {
//                        for (int i = 0; i < response.length(); i++) {
//                            cars.add(response.getString(i)); // Directly get the string value
//                        }
//                        ArrayAdapter<String> carAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
//                        carAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        carSpinner.setAdapter(carAdapter);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(this, "Error parsing cars", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                error -> Toast.makeText(this, "Error fetching cars", Toast.LENGTH_SHORT).show());
//        queue.add(jsonArrayRequest);
//    }

    private void submitBooking() {
        if (carSpinner.getAdapter() == null || carSpinner.getCount() == 0) {
            Toast.makeText(this, "No cars available. Please add a car before booking a service.", Toast.LENGTH_SHORT).show();
            return;
        }

        String serviceName = serviceSpinner.getSelectedItem().toString();
        String licensePlate = carSpinner.getSelectedItem().toString();
        String emergencyLevel = emergencySpinner.getSelectedItem().toString();

        String url = "http://" + IP + "/CAR_MAINTENANCE/book_service.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Booking successful", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error booking service", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(ssn));
                params.put("serviceName", serviceName);
                params.put("licensePlate", licensePlate);
                params.put("emergencyLevel", emergencyLevel);
                params.put("status", "pending");
                params.put("submittedDate", getCurrentDate());
                return params;
            }
        };
        queue.add(stringRequest);
    }
//    private void submitBooking() {
//        String serviceName = serviceSpinner.getSelectedItem().toString();
//        String licensePlate = carSpinner.getSelectedItem().toString();
//        String emergencyLevel = emergencySpinner.getSelectedItem().toString();
//
//        String url = "http://"+IP+"/CAR_MAINTENANCE/book_service.php";
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                response -> Toast.makeText(this, "Booking successful", Toast.LENGTH_SHORT).show(),
//                error -> Toast.makeText(this, "Error booking service", Toast.LENGTH_SHORT).show()) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("userId", String.valueOf(ssn));
//                params.put("serviceName", serviceName);
//                params.put("licensePlate", licensePlate);
//                params.put("emergencyLevel", emergencyLevel);
//                params.put("status", "pending");
//                params.put("submittedDate", getCurrentDate());
//                return params;
//            }
//        };
//        queue.add(stringRequest);
//    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    }
    public void homePage(View view) {
        Intent intent = new Intent(BookServiceActivity.this, CustomerDashboardActivity.class);
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
        Intent intent = new Intent(BookServiceActivity.this, ViewTicketsActivity.class);
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
        Intent intent = new Intent(BookServiceActivity.this, BookServiceActivity.class);
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