package com.example.car_maintenance_project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomerDashboardActivity extends AppCompatActivity {
    private TableLayout carsTable;
    private TableLayout ticketsTable;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        carsTable = findViewById(R.id.cars_card_table);
        ticketsTable = findViewById(R.id.history_card_table);
        ImageView profile = findViewById(R.id.right_image);

        Intent incomingIntent = getIntent();
        ssn = incomingIntent.getStringExtra("ssn");
        firstname = incomingIntent.getStringExtra("first_name");
        lastname = incomingIntent.getStringExtra("last_name");
        email = incomingIntent.getStringExtra("email");
        password = incomingIntent.getStringExtra("password");
        gender = incomingIntent.getStringExtra("gender");
        type = incomingIntent.getStringExtra("type");
        address = incomingIntent.getStringExtra("address");
        phonenumber = incomingIntent.getStringExtra("phone_number");
        dateofbirth = incomingIntent.getStringExtra("date_of_birth");
        createdAt = incomingIntent.getStringExtra("created_at");
        updatedAt = incomingIntent.getStringExtra("updated_at");

        // Set click listener for Settings button
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(CustomerDashboardActivity.this, ProfileSetting.class);
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

        TextView feedback = findViewById(R.id.feedback_link);

        // Set click listener for Feedback button
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedbackIntent = new Intent(CustomerDashboardActivity.this, feedback.class);
                feedbackIntent.putExtra("ssn", ssn);
                startActivityForResult(feedbackIntent, 1);
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

        fetchCarsData("http://"+IP+"/CAR_MAINTENANCE/get_customer_cars.php?userId=" + ssn);
        fetchCompletedTicketsData("http://"+IP+"/CAR_MAINTENANCE/get_completed_tickets.php?userId=" + ssn);
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
                                populateCarsTable(carsArray);
                            } else if (response.has("error") && response.getBoolean("error")) {
                                // Display the error message if it exists
                                String message = response.getString("message");
                                Toast.makeText(CustomerDashboardActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CustomerDashboardActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerDashboardActivity.this, "Error fetching cars: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }

    private void fetchCompletedTicketsData(String url) {
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
                                populateTicketsTable(ticketsArray);
                            } else if (response.has("error") && response.getBoolean("error")) {
                                // Display the error message if it exists
                                String message = response.getString("message");
                                Toast.makeText(CustomerDashboardActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CustomerDashboardActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerDashboardActivity.this, "Error fetching tickets: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }


//    private void fetchCompletedTicketsData(String url) {
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        populateTicketsTable(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Toast.makeText(CustomerDashboardActivity.this, "Error fetching tickets: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        queue.add(jsonArrayRequest);
//    }

    private void populateCarsTable(JSONArray carsData) {
        carsTable.removeAllViews();
        int numOfViews = Math.min(carsData.length(), 2);

        for (int i = 0; i < numOfViews; i++) {
            try {
                JSONObject car = carsData.getJSONObject(i);
                String make = car.getString("make");
                String model = car.getString("model");
                String year = car.getString("year");

                TableRow row = createTableRow();
                row.addView(createImageView(R.drawable.mini_car));
                row.addView(createStyledTextView(make));
                row.addView(createStyledTextView(model));
                row.addView(createStyledTextView(year));

                carsTable.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateTicketsTable(JSONArray ticketsData) {
        ticketsTable.removeAllViews();
        int numOfViews = Math.min(ticketsData.length(), 2);

        for (int i = 0; i < numOfViews; i++) {
            try {
                JSONObject ticket = ticketsData.getJSONObject(i);
                String service = ticket.getString("service_id");
                String completedDate = ticket.getString("completed_date");

                TableRow row = createTableRow();
                row.addView(createImageView(R.drawable.ticket_icon)); // Replace with ticket icon resource
                row.addView(createStyledTextView(service));
                row.addView(createStyledTextView(completedDate));

                ticketsTable.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private TableRow createTableRow() {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        row.setLayoutParams(rowParams);
        row.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return row;
    }

    private ImageView createImageView(int drawableRes) {
        ImageView gifView = new ImageView(this);
        gifView.setLayoutParams(new TableRow.LayoutParams(100, 100));
        Glide.with(this).load(drawableRes).into(gifView);
        return gifView;
    }

    private TextView createStyledTextView(String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1
        ));
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(8, 8, 8, 8);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextColor(Color.parseColor("#003049"));
        return textView;
    }

    public void showMoreCars(View view) {
        Intent intent = new Intent(CustomerDashboardActivity.this, CustomerCars.class);
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

    public void homePage(View view) {
        Intent intent = new Intent(CustomerDashboardActivity.this, CustomerDashboardActivity.class);
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

    public void completedTicketsPage(View view) {
        Intent intent = new Intent(CustomerDashboardActivity.this, CompletedTicketsActivity.class);
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
        Intent intent = new Intent(CustomerDashboardActivity.this, ViewTicketsActivity.class);
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

    public void bookServicePage(View view) {
        Intent intent = new Intent(CustomerDashboardActivity.this, BookServiceActivity.class);
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