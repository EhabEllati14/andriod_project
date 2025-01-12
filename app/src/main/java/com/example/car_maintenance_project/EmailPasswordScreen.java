package com.example.car_maintenance_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailPasswordScreen extends AppCompatActivity {
    private EditText email, password, confirmpassword;
    private Button create, cancel;
    private final String IP = "192.168.2.73";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password_screen);

        Intent intent = getIntent();

        // Retrieve data passed from the previous screen
        String firstName = intent.getStringExtra("FIRSTNAME");
        String lastName = intent.getStringExtra("LASTNAME");
        String ssn = intent.getStringExtra("SSN");
        String birthDate = intent.getStringExtra("BIRTHDATE");
        String address = intent.getStringExtra("ADDRESS");
        String gender = intent.getStringExtra("GENDER");
        String phoneNumber = intent.getStringExtra("PHONENUMBER");

        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        create = findViewById(R.id.send);
        cancel = findViewById(R.id.cancel);

        // Handle Cancel Button
        cancel.setOnClickListener(view -> new AlertDialog.Builder(EmailPasswordScreen.this)
                .setTitle("Cancel Registration")
                .setMessage("Are you sure you want to cancel?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    password.setText("");
                    email.setText("");
                    confirmpassword.setText("");
                    finish(); // Close the activity
                })
                .setNegativeButton("No", null)
                .show());

        // Handle Create Button
        create.setOnClickListener(view -> {
            // Email Validation
            if (email.getText().toString().isEmpty()) {
                Toast.makeText(EmailPasswordScreen.this, "Please Enter The Email", Toast.LENGTH_LONG).show();
                return;
            } else if (!email.getText().toString().contains("@")) {
                Toast.makeText(EmailPasswordScreen.this, "Invalid Email\n Should Contain @ sign", Toast.LENGTH_LONG).show();
                return;
            } else if (!(email.getText().toString().endsWith("@gmail.com") || email.getText().toString().endsWith("@hotmail.com"))) {
                Toast.makeText(EmailPasswordScreen.this, "Email should End with\n @gmail or @hotmail.com", Toast.LENGTH_LONG).show();
                return;
            }

            // Check if email exists
            String emailUrl = "http://" + IP + "/car_maintenance/emailexists.php?email=" + email.getText().toString();
            StringRequest emailRequest = new StringRequest(Request.Method.GET, emailUrl,
                    response -> {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            boolean error = jsonObj.getBoolean("error");
                            String msg = jsonObj.getString("msg");
                            if (error) {
                                Toast.makeText(EmailPasswordScreen.this, msg, Toast.LENGTH_LONG).show();
                                email.setText("");
                            } else {
                                Toast.makeText(EmailPasswordScreen.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(EmailPasswordScreen.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

            Volley.newRequestQueue(EmailPasswordScreen.this).add(emailRequest);

            // Password Validation
            if (password.getText().toString().isEmpty()) {
                Toast.makeText(EmailPasswordScreen.this, "Please Enter The Password", Toast.LENGTH_LONG).show();
                return;
            } else if (!password.getText().toString().equals(confirmpassword.getText().toString())) {
                Toast.makeText(EmailPasswordScreen.this, "Passwords do not match. Please fill them again!", Toast.LENGTH_LONG).show();
                password.setText("");
                confirmpassword.setText("");
                return;
            }

            // Proceed to save data if all validations pass
            String saveUrl = "http://" + IP + "/car_maintenance/Signup.php";
            RequestQueue queue = Volley.newRequestQueue(EmailPasswordScreen.this);

            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("ssn", ssn);
                jsonParams.put("first_name", firstName);
                jsonParams.put("last_name", lastName);
                jsonParams.put("password", password.getText().toString());
                jsonParams.put("gender", gender);
                jsonParams.put("email", email.getText().toString());
                jsonParams.put("address", address);
                jsonParams.put("phone_number", phoneNumber);
                jsonParams.put("date_of_birth", birthDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest saveRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    saveUrl,
                    jsonParams,
                    response -> {
                        try {
                            boolean error = response.getBoolean("error");
                            String msg = response.getString("msg");

                            if (!error) {
                                Toast.makeText(EmailPasswordScreen.this, msg, Toast.LENGTH_SHORT).show();
                                email.setText("");
                                password.setText("");
                                confirmpassword.setText("");

                                // Redirect to Login Page
                                Intent intent1 = new Intent(EmailPasswordScreen.this, login.class);
                                startActivity(intent1);
                                finish(); // Close current activity
                            } else {
                                Toast.makeText(EmailPasswordScreen.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.d("VolleyError", error.toString());
                        Toast.makeText(EmailPasswordScreen.this, "Request Failed", Toast.LENGTH_SHORT).show();
                    });

            queue.add(saveRequest);
        });
    }
}
