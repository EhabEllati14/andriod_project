package com.example.car_maintenance_project;

import model.Ticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class ViewTicketsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewTicketsAdapter adapter;
    private List<Ticket> tickets;
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
        setContentView(R.layout.activity_view_tickets); // Use the new layout
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
        ImageView profile = findViewById(R.id.right_image);

        // Set click listener for Settings button
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(ViewTicketsActivity.this, ProfileSetting.class);
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
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.view_requests_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize list and adapter
        tickets = new ArrayList<>();
        adapter = new ViewTicketsAdapter(tickets);
        recyclerView.setAdapter(adapter);

        // Fetch tickets data if userId is valid
        if (ssn != "") {
            fetchTicketsData("http://"+IP+"/CAR_MAINTENANCE/get_tickets.php?userId=" + ssn);
        } else {
            Toast.makeText(this, "No user ID provided!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchTicketsData(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("tickets")) {
                                // Extract the tickets array from the response
                                JSONArray ticketsArray = response.getJSONArray("tickets");
                                parseTicketsResponse(ticketsArray);
                            } else if (response.has("error") && response.getBoolean("error")) {
                                // Display the error message if it exists
                                String message = response.getString("message");
                                Toast.makeText(ViewTicketsActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewTicketsActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewTicketsActivity.this, "Error fetching tickets: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }

//    private void fetchTicketsData(String url) {
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        parseTicketsResponse(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ViewTicketsActivity.this, "Error fetching tickets: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        queue.add(jsonArrayRequest);
//    }

    private void parseTicketsResponse(JSONArray response) {
        tickets.clear();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject ticketJson = response.getJSONObject(i);
                int ticketId = ticketJson.getInt("ticket_id");
                String serviceName = ticketJson.getString("service_provided");
                String submittedDate = ticketJson.getString("submitted_date");
                String customerName = ticketJson.getString("customer_name");
                String emergencyLevel = ticketJson.getString("emergency_level");
                String status = ticketJson.getString("status");

                tickets.add(new Ticket(ticketId, serviceName, submittedDate, customerName, emergencyLevel, status));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    public void homePage(View view) {
        Intent intent = new Intent(ViewTicketsActivity.this, CustomerDashboardActivity.class);
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

    public void viewTicketsPage(View view) {
        Intent intent = new Intent(ViewTicketsActivity.this, ViewTicketsActivity.class);
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
        Intent intent = new Intent(ViewTicketsActivity.this, BookServiceActivity.class);
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