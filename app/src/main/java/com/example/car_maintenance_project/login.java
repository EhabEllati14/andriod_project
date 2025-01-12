package com.example.car_maintenance_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class login extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private CheckBox rememberMe;
    private TextView registerText;
    private boolean isPasswordVisible = false;
    private final String IP = "192.168.2.73";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.loginorientation);
        } else {
            setContentView(R.layout.activity_main);
        }

        initializeUI();
        setupSharedPreferences();
        restoreState();

        loginButton.setOnClickListener(view -> handleLogin());
        registerText.setOnClickListener(view -> navigateToRegister());

        passwordEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });
    }

    private void initializeUI() {
        emailEditText = findViewById(R.id.lastname);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbtn);
        rememberMe = findViewById(R.id.remeberme);
        registerText = findViewById(R.id.register);
    }

    private void setupSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
    }

    private void restoreState() {
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            emailEditText.setText(sharedPreferences.getString("email", ""));
            passwordEditText.setText(sharedPreferences.getString("password", ""));
            rememberMe.setChecked(true);
        }
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@")) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://" + IP + "/car_maintenance/login.php?email=" + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> processLoginResponse(response, email, password),
                error -> Toast.makeText(this, "Network error: " + error, Toast.LENGTH_SHORT).show());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void processLoginResponse(String response, String email, String password) {
        try {
            JSONObject jsonUser = new JSONObject(response);

            // Retrieve all user data from JSON response
            String emailDb = jsonUser.getString("email");
            String passwordDb = jsonUser.getString("password");

            if (email.equals(emailDb) && password.equals(passwordDb)) {
                if (rememberMe.isChecked()) {
                    saveCredentials(email, password);
                }

                // Extract all user details
                String ssn = jsonUser.getString("ssn");
                String firstName = jsonUser.getString("first_name");
                String lastName = jsonUser.getString("last_name");
                String gender = jsonUser.getString("gender");
                String type = jsonUser.getString("type");
                String address = jsonUser.getString("address");
                String phoneNumber = jsonUser.getString("phone_number");
                String dateOfBirth = jsonUser.getString("date_of_birth");
                String createdAt = jsonUser.getString("created_at");
                String updatedAt = jsonUser.getString("updated_at");

                // Pass user details to dashboard
                Intent intent = new Intent(this, CustomerDashboardActivity.class);
                intent.putExtra("ssn", ssn);
                intent.putExtra("first_name", firstName);
                intent.putExtra("last_name", lastName);
                intent.putExtra("email", emailDb);
                intent.putExtra("gender", gender);
                intent.putExtra("type", type);
                intent.putExtra("address", address);
                intent.putExtra("phone_number", phoneNumber);
                intent.putExtra("date_of_birth", dateOfBirth);
                intent.putExtra("created_at", createdAt);
                intent.putExtra("updated_at", updatedAt);
                startActivity(intent);

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                emailEditText.setText("");
                passwordEditText.setText("");
            }
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e);
        }
    }

    private void saveCredentials(String email, String password) {
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void navigateToRegister() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye, 0);
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

}
