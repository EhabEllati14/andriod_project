package com.example.car_maintenance_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class changepassword extends AppCompatActivity {
    private EditText password, confirmpassword;
    private Button ok, cancel;
    private final String IP = "192.168.1.25";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_changepassword);

        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        ok = findViewById(R.id.send);
        cancel = findViewById(R.id.cancel);

        ok.setOnClickListener(view -> {
            String newPassword = password.getText().toString().trim();
            String confirmPassword = confirmpassword.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(changepassword.this, "Passwords cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(changepassword.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                password.setText("");
                confirmpassword.setText("");
                return;
            }

            Intent intent1 = getIntent();
            String ssn = intent1.getStringExtra("ssn");

            checkIfPasswordExists(newPassword, ssn);
        });

        cancel.setOnClickListener(view -> {
            new AlertDialog.Builder(changepassword.this)
                    .setTitle("Cancel Feedback")
                    .setMessage("Are you sure you want to cancel?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        password.setText("");
                        confirmpassword.setText("");
                        finish();
                    })
                    .setNegativeButton("No", null) // Do nothing if "No" is selected
                    .show();
        });
    }

    private void checkIfPasswordExists(String newPassword, String ssn) {
        String url = "http://"+IP+"/car_maintenance/passwordExists.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject passwordParams = new JSONObject();
        try {
            passwordParams.put("password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                passwordParams,
                response -> {
                    try {
                        boolean error = response.getBoolean("error");
                        String msg = response.getString("msg");

                        if (error) {
                            Toast.makeText(changepassword.this, msg, Toast.LENGTH_LONG).show();
                            password.setText("");
                            confirmpassword.setText("");
                        } else {
                            changePasswordInDatabase(newPassword, ssn);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(changepassword.this, "Response Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.d("VolleyError", error.toString());
                    Toast.makeText(changepassword.this, "Network Error: Request Failed", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonObjectRequest);
    }

    private void changePasswordInDatabase(String newPassword, String ssn) {
        String url = "http://"+IP+"/car_maintenance/savepassword.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("ssn", ssn);
            jsonParams.put("password", newPassword);
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
                            Toast.makeText(changepassword.this, msg, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(changepassword.this, ProfileSetting.class);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(changepassword.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(changepassword.this, "Response Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.d("VolleyError", error.toString());
                    Toast.makeText(changepassword.this, "Network Error: Request Failed", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}
