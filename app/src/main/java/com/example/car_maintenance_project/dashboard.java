package com.example.car_maintenance_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class dashboard extends AppCompatActivity {
    private Button setting, feedback;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        setting = findViewById(R.id.settings);
        feedback = findViewById(R.id.feedback);

        // Retrieve data passed to this activity
        Intent incomingIntent = getIntent();
        String ssn = incomingIntent.getStringExtra("ssn");
        String firstname = incomingIntent.getStringExtra("first_name");
        String lastname = incomingIntent.getStringExtra("last_name");
        String email = incomingIntent.getStringExtra("email");
        String password = incomingIntent.getStringExtra("password");
        String gender = incomingIntent.getStringExtra("gender");
        String type = incomingIntent.getStringExtra("type");
        String address = incomingIntent.getStringExtra("address");
        String phonenumber = incomingIntent.getStringExtra("phone_number");
        String dateofbirth = incomingIntent.getStringExtra("date_of_birth");
        String createdAt = incomingIntent.getStringExtra("created_at");
        String updatedAt = incomingIntent.getStringExtra("updated_at");
        feedback = findViewById(R.id.feedback);

        // Set click listener for Feedback button
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedbackIntent = new Intent(dashboard.this, feedback.class);
                feedbackIntent.putExtra("ssn",ssn);
               startActivityForResult(feedbackIntent,1);
            }
        });

        // Set click listener for Settings button
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(dashboard.this, ProfileSetting.class);
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
    }
}
