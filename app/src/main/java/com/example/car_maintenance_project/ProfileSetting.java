package com.example.car_maintenance_project;
//
//import android.app.DatePickerDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.CircleCrop;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;
//
//public class ProfileSetting extends AppCompatActivity {
//    private EditText firstname1, lastname1, emails, phonenumbers, birthdate;
//    private Spinner address1, gender1;
//    private Button changepasswordbtn, savebtn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_profile_setting);
//
//        firstname1 = findViewById(R.id.firstname);
//        lastname1 = findViewById(R.id.lastname);
//        emails = findViewById(R.id.email);
//        address1 = findViewById(R.id.address_spinner);
//        gender1 = findViewById(R.id.gender_spinner);
//        phonenumbers = findViewById(R.id.phonenumber);
//        birthdate = findViewById(R.id.birthdate_input);
//        changepasswordbtn = findViewById(R.id.Passwordchange);
//        savebtn = findViewById(R.id.savechanges);
//
//        Intent intent = getIntent();
//
//        String ssn = intent.getStringExtra("ssn");
//        String firstname = intent.getStringExtra("first_name");
//        String lastname = intent.getStringExtra("last_name");
//        String email = intent.getStringExtra("email");
//        String gender = intent.getStringExtra("gender");
//        String address = intent.getStringExtra("address");
//        String phonenumber = intent.getStringExtra("phone_number");
//        String dateofbirth = intent.getStringExtra("date_of_birth");
//        ImageView circleImageView = findViewById(R.id.circleImageView1);
//
//        if ("female".equalsIgnoreCase(gender)) {
//            Glide.with(this)
//                    .asGif()
//                    .load(R.drawable.avatar)
//                    .transform(new CircleCrop())
//                    .into(circleImageView);
//        } else {
//            Glide.with(this)
//                    .asGif()
//                    .load(R.drawable.groom)
//                    .transform(new CircleCrop())
//                    .into(circleImageView);
//        }
//
//        firstname1.setText(firstname);
//        lastname1.setText(lastname);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this, R.array.palestinian_cities_names, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        address1.setAdapter(adapter);
//
//        if (address != null) {
//            int position = adapter.getPosition(address);
//            if (position != -1) {
//                address1.setSelection(position);
//            }
//        }
//
//        if ("male".equalsIgnoreCase(gender)) {
//            gender1.setSelection(1);
//        } else {
//            gender1.setSelection(2);
//        }
//
//        emails.setText(email);
//        birthdate.setText(dateofbirth);
//        phonenumbers.setText(phonenumber);
//
//        birthdate.setOnClickListener(v -> showDatePicker());
//
//        changepasswordbtn.setOnClickListener(view -> {
//            Intent intent1 = new Intent(ProfileSetting.this, changepassword.class);
//            intent1.putExtra("ssn", ssn);
//            startActivityForResult(intent1, 1);
//        });
//
//        savebtn.setOnClickListener(view -> {
//            if (validateFields(ssn)) {
//                saveUserChanges(ssn);
//            }
//        });
//    }
//
//    private boolean validateFields(String ssn) {
//        if (firstname1.getText().toString().trim().isEmpty()) {
//            Toast.makeText(this, "Please Fill The First Name", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (lastname1.getText().toString().trim().isEmpty()) {
//            Toast.makeText(this, "Please Fill The Last Name", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (gender1.getSelectedItemPosition() == 0 || gender1.getSelectedItem().toString().equals("Select a Gender")) {
//            Toast.makeText(this, "Please Select Your Gender", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (address1.getSelectedItemPosition() == 0 || address1.getSelectedItem().toString().equals("Select an Address")) {
//            Toast.makeText(this, "Please Select Your Address", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (phonenumbers.getText().toString().trim().isEmpty()) {
//            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (!phonenumbers.getText().toString().trim().matches("\\d{10}")) {
//            Toast.makeText(this, "Phone Number Should be 10 digits", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (birthdate.getText().toString().trim().isEmpty()) {
//            Toast.makeText(this, "Please Enter Birth Date", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (!birthdate.getText().toString().isEmpty() &&
//                Integer.parseInt(birthdate.getText().toString().split("-")[0]) >= 2003) {
//            Toast.makeText(this, "You must be born before 2003", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (emails.getText().toString().trim().isEmpty()) {
//            Toast.makeText(this, "Please Enter The Email", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (!emails.getText().toString().contains("@")) {
//            Toast.makeText(this, "Invalid Email\nShould Contain @ sign", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (!(emails.getText().toString().endsWith("@gmail.com") || emails.getText().toString().endsWith("@hotmail.com"))) {
//            Toast.makeText(this, "Email should End with\n@gmail or @hotmail.com", Toast.LENGTH_LONG).show();
//            return false;
//        } else {
//            checkEmailExists(emails.getText().toString(), ssn);
//        }
//        return true;
//    }
//
//    private void checkEmailExists(String email, String ssn) {
//        String emailUrl = "http://192.168.1.101/car_maintenance/emailexists.php?email=" + email;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, emailUrl,
//                response -> {
//                    try {
//                        JSONObject jsonObj = new JSONObject(response);
//                        boolean error = jsonObj.getBoolean("error");
//                        String msg = jsonObj.getString("msg");
//
//                        if (error) {
//                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//                            emails.setText("");
//                        } else {
//                            Toast.makeText(this, "Email is available", Toast.LENGTH_SHORT).show();
//                            saveUserChanges(ssn);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());
//
//        Volley.newRequestQueue(this).add(stringRequest);
//    }


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileSetting extends AppCompatActivity {
    private EditText firstname1, lastname1, emails, phonenumbers, birthdate;
    private Spinner address1, gender1;
    private Button changepasswordbtn, savebtn ,logoutbtn;
    private final String IP = "192.168.1.101";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_setting);

        firstname1 = findViewById(R.id.firstname);
        lastname1 = findViewById(R.id.lastname);
        emails = findViewById(R.id.email);
        address1 = findViewById(R.id.address_spinner);
        gender1 = findViewById(R.id.gender_spinner);
        phonenumbers = findViewById(R.id.phonenumber);
        birthdate = findViewById(R.id.birthdate_input);
        changepasswordbtn = findViewById(R.id.Passwordchange);
        savebtn = findViewById(R.id.savechanges);
        logoutbtn=findViewById(R.id.logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileSetting.this,login.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();



        String ssn = intent.getStringExtra("ssn");
        String firstname = intent.getStringExtra("first_name");
        String lastname = intent.getStringExtra("last_name");
        String email = intent.getStringExtra("email");
        String gender = intent.getStringExtra("gender");
        String address = intent.getStringExtra("address");
        String phonenumber = intent.getStringExtra("phone_number");
        String dateofbirth = intent.getStringExtra("date_of_birth");
        ImageView circleImageView = findViewById(R.id.circleImageView1);

        if ("female".equalsIgnoreCase(gender)) {
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.avatar)
                    .transform(new CircleCrop())
                    .into(circleImageView);
        } else {
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.groom)
                    .transform(new CircleCrop())
                    .into(circleImageView);
        }

        firstname1.setText(firstname);
        lastname1.setText(lastname);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.palestinian_cities_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        address1.setAdapter(adapter);

        if (address != null) {
            int position = adapter.getPosition(address);
            if (position != -1) {
                address1.setSelection(position);
            }
        }

        if ("male".equalsIgnoreCase(gender)) {
            gender1.setSelection(1);
        } else {
            gender1.setSelection(2);
        }

        emails.setText(email);
        birthdate.setText(dateofbirth);
        phonenumbers.setText(phonenumber);

        birthdate.setOnClickListener(v -> showDatePicker());

        emails.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                String enteredEmail = emails.getText().toString().trim();
                if (!enteredEmail.isEmpty() && enteredEmail.contains("@")) {
                    checkEmailOnFocus(enteredEmail, ssn);
                }
            }
        });

        changepasswordbtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(ProfileSetting.this, changepassword.class);
            intent1.putExtra("ssn", ssn);
            startActivityForResult(intent1, 1);
        });

        savebtn.setOnClickListener(view -> {
            if (validateFields()) {
                saveUserChanges(ssn);
            }
        });
    }

    private boolean validateFields() {
        if (firstname1.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Fill The First Name", Toast.LENGTH_LONG).show();
            return false;
        } else if (lastname1.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Fill The Last Name", Toast.LENGTH_LONG).show();
            return false;
        } else if (gender1.getSelectedItemPosition() == 0 || gender1.getSelectedItem().toString().equals("Select a Gender")) {
            Toast.makeText(this, "Please Select Your Gender", Toast.LENGTH_LONG).show();
            return false;
        } else if (address1.getSelectedItemPosition() == 0 || address1.getSelectedItem().toString().equals("Select an Address")) {
            Toast.makeText(this, "Please Select Your Address", Toast.LENGTH_LONG).show();
            return false;
        } else if (phonenumbers.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_LONG).show();
            return false;
        } else if (!phonenumbers.getText().toString().trim().matches("\\d{10}")) {
            Toast.makeText(this, "Phone Number Should be 10 digits", Toast.LENGTH_LONG).show();
            return false;
        } else if (birthdate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Birth Date", Toast.LENGTH_LONG).show();
            return false;
        } else if (!birthdate.getText().toString().isEmpty() &&
                Integer.parseInt(birthdate.getText().toString().split("-")[0]) >= 2003) {
            Toast.makeText(this, "You must be born before 2003", Toast.LENGTH_LONG).show();
            return false;
        } else if (emails.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter The Email", Toast.LENGTH_LONG).show();
            return false;
        } else if (!emails.getText().toString().contains("@")) {
            Toast.makeText(this, "Invalid Email\nShould Contain @ sign", Toast.LENGTH_LONG).show();
            return false;
        } else if (!(emails.getText().toString().endsWith("@gmail.com") || emails.getText().toString().endsWith("@hotmail.com"))) {
            Toast.makeText(this, "Email should End with\n@gmail or @hotmail.com", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void checkEmailOnFocus(String email, String ssn) {
        String emailUrl = "http://"+IP+"/car_maintenance/emailexists.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject emailParams = new JSONObject();
        try {
            emailParams.put("email", email);
            emailParams.put("ssn", ssn);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                emailUrl,
                emailParams,
                response -> {
                    try {
                        boolean error = response.getBoolean("error");
                        String msg = response.getString("msg");

                        if (error) {
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                            emails.setText("");
                        } else {
                            Toast.makeText(this, "Email is valid", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    private void saveUserChanges(String ssn) {
        String url = "http://"+IP+"/car_maintenance/profilesetting.php";
        RequestQueue queue = Volley.newRequestQueue(ProfileSetting.this);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("ssn", ssn);
            jsonParams.put("first_name", firstname1.getText().toString());
            jsonParams.put("last_name", lastname1.getText().toString());
            jsonParams.put("gender", gender1.getSelectedItem().toString());
            jsonParams.put("email", emails.getText().toString());
            jsonParams.put("address", address1.getSelectedItem().toString());
            jsonParams.put("phone_number", phonenumbers.getText().toString());
            jsonParams.put("date_of_birth", birthdate.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonParams,
                response -> {
                    try {
                        boolean error = response.getBoolean("error");
                        String msg = response.getString("msg");

                        if (!error) {
                            Toast.makeText(ProfileSetting.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileSetting.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d("VolleyError", error.toString());
                    Toast.makeText(ProfileSetting.this, "Request Failed", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = formatDate(selectedYear, selectedMonth + 1, selectedDay);
                    birthdate.setText(formattedDate);
                }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return sdf.format(calendar.getTime());
    }
}
