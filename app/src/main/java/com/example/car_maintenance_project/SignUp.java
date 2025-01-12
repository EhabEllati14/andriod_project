package com.example.car_maintenance_project;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUp extends AppCompatActivity {

    private Button next, clear, back;
    private EditText firstname, lastname, ssn, birthdateInput, phonenumber;
    private Spinner address, gender;
    private final String IP = "192.168.2.73";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.signup_change_orientation);
        } else {
            setContentView(R.layout.activity_sign_up);
        }
        initializeUI();

        birthdateInput.setOnClickListener(v -> showDatePicker());

        back.setOnClickListener(view -> showBackDialog());

        next.setOnClickListener(view -> validateAndProceed());

        clear.setOnClickListener(view -> clearFields());
    }

    private void initializeUI() {
        birthdateInput = findViewById(R.id.birthdate_input);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        ssn = findViewById(R.id.ssn_input);
        address = findViewById(R.id.address_spinner);
        gender = findViewById(R.id.gender_spinner);
        phonenumber = findViewById(R.id.phonenumber);
        next = findViewById(R.id.btnNext);
        clear = findViewById(R.id.btnClear);
        back = findViewById(R.id.btnBack);
        birthdateInput.setText("");
        firstname.setText("");
        lastname.setText("");
        ssn.setText("");
        address.setSelection(0);
        gender.setSelection(0);
        phonenumber.setText("");
    }

    private void showBackDialog() {
        new AlertDialog.Builder(SignUp.this)
                .setTitle("Cancel Sign-Up")
                .setMessage("Are you sure you want to cancel? All entered data will be lost.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    clearFields();
                    navigateToLogin();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clearFields() {
        birthdateInput.setText("");
        firstname.setText("");
        lastname.setText("");
        ssn.setText("");
        address.setSelection(0);
        gender.setSelection(0);
        phonenumber.setText("");
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignUp.this, login.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = formatDate(selectedYear, selectedMonth + 1, selectedDay);
                    birthdateInput.setText(formattedDate);
                }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return sdf.format(calendar.getTime());
    }

    private void validateAndProceed() {
        if (firstname.getText().toString().isEmpty()) {
            Toast.makeText(SignUp.this, "Please Fill The First Name", Toast.LENGTH_LONG).show();
        } else if (lastname.getText().toString().isEmpty()) {
            Toast.makeText(SignUp.this, "Please Fill The Last Name", Toast.LENGTH_LONG).show();
        } else if (gender.getSelectedItemPosition() == 0) {
            Toast.makeText(SignUp.this, "Please Select Your Gender", Toast.LENGTH_LONG).show();
        } else if (ssn.getText().toString().isEmpty()) {
            Toast.makeText(SignUp.this, "Please Enter SSN Number", Toast.LENGTH_LONG).show();
        } else if (!ssn.getText().toString().trim().matches("\\d{9}")) {
            Toast.makeText(SignUp.this, "SSN Number Should be 9 digits", Toast.LENGTH_LONG).show();
        } else {
            String ssnUrl = "http://" + IP + "/car_maintenance/ssncheck.php?ssn=" + ssn.getText().toString();
            RequestQueue queue = Volley.newRequestQueue(SignUp.this);

            StringRequest ssnRequest = new StringRequest(Request.Method.GET, ssnUrl,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean("error");
                            String msg = jsonResponse.getString("msg");

                            if (!error) {
                                validateNextFields();
                            } else {
                                Toast.makeText(SignUp.this, msg, Toast.LENGTH_LONG).show();
                                ssn.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.d("VolleyError", error.toString());
                        Toast.makeText(SignUp.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    });

            queue.add(ssnRequest);
        }
    }

    private void validateNextFields() {
        if (birthdateInput.getText().toString().isEmpty()) {
            Toast.makeText(SignUp.this, "Please Enter Birth Date", Toast.LENGTH_LONG).show();
        } else if (!birthdateInput.getText().toString().isEmpty() &&
                Integer.parseInt(birthdateInput.getText().toString().split("-")[0]) >= 2003) {
            Toast.makeText(SignUp.this, "You must be born before 2003", Toast.LENGTH_LONG).show();
        } else if (address.getSelectedItemPosition() == 0) {
            Toast.makeText(SignUp.this, "Please Select Your Address", Toast.LENGTH_LONG).show();
        } else if (phonenumber.getText().toString().isEmpty()) {
            Toast.makeText(SignUp.this, "Please Enter Phone Number", Toast.LENGTH_LONG).show();
        } else if (!phonenumber.getText().toString().trim().matches("\\d{10}")) {
            Toast.makeText(SignUp.this, "Phone Number Should be 10 digits", Toast.LENGTH_LONG).show();
        } else {
            String phoneUrl = "http://" + IP + "/car_maintenance/phonenumbercheck.php?phone_number=" + phonenumber.getText().toString();
            RequestQueue queue = Volley.newRequestQueue(SignUp.this);
            StringRequest phoneRequest = new StringRequest(Request.Method.GET, phoneUrl,
                    response -> {
                        try {
                            JSONObject phoneResponse = new JSONObject(response);
                            boolean phoneError = phoneResponse.getBoolean("error");
                            String phoneMsg = phoneResponse.getString("msg");

                            if (!phoneError) {
                                Intent intent = new Intent(SignUp.this, EmailPasswordScreen.class);
                                intent.putExtra("FIRSTNAME", firstname.getText().toString());
                                intent.putExtra("LASTNAME", lastname.getText().toString());
                                intent.putExtra("SSN", ssn.getText().toString());
                                intent.putExtra("BIRTHDATE", birthdateInput.getText().toString());
                                intent.putExtra("ADDRESS", address.getSelectedItem().toString());
                                intent.putExtra("GENDER", gender.getSelectedItem().toString());
                                intent.putExtra("PHONENUMBER", phonenumber.getText().toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUp.this, phoneMsg, Toast.LENGTH_LONG).show();
                                phonenumber.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.d("VolleyError", error.toString());
                        Toast.makeText(SignUp.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    });

            queue.add(phoneRequest);
        }
    }

    // Lifecycle callbacks
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("firstname", firstname.getText().toString());
        outState.putString("lastname", lastname.getText().toString());
        outState.putString("ssn", ssn.getText().toString());
        outState.putString("birthdate", birthdateInput.getText().toString());
        outState.putString("phonenumber", phonenumber.getText().toString());
        outState.putInt("address", address.getSelectedItemPosition());
        outState.putInt("gender", gender.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        firstname.setText(savedInstanceState.getString("firstname"));
        lastname.setText(savedInstanceState.getString("lastname"));
        ssn.setText(savedInstanceState.getString("ssn"));
        birthdateInput.setText(savedInstanceState.getString("birthdate"));
        phonenumber.setText(savedInstanceState.getString("phonenumber"));
        address.setSelection(savedInstanceState.getInt("address"));
        gender.setSelection(savedInstanceState.getInt("gender"));
    }
}
