package com.example.car_maintenance_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class feedback extends AppCompatActivity {
    private Button submit, cancel;
    private ImageView[] stars = new ImageView[5];
    private  static int currentRating = 0;
    private EditText message;
    private final String IP = "192.168.2.73";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);
        Intent res=getIntent();
        String ssn = res.getStringExtra("ssn");
Log.d("ssn","ssn --->>>>>>>>>>>>>>>>>>>>>>>>"+ssn);

        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);

        cancel=findViewById(R.id.cancel);
        message=findViewById(R.id.message);

        cancel.setOnClickListener(view -> {
            new AlertDialog.Builder(feedback.this)
                    .setTitle("Cancel Feedback")
                    .setMessage("Are you sure you want to cancel?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });


        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int stars = currentRating;

                String msg = message.getText().toString();
                if (msg.equals("")) {
                    msg = "None";
                }

                String url = "http://"+IP+"/car_maintenance/feedback.php";
                RequestQueue queue = Volley.newRequestQueue(feedback.this);
                JSONObject jsonParams = new JSONObject();

                try {
                    jsonParams.put("ssn", ssn);
                    jsonParams.put("message", msg);
                    if(currentRating==0){
                        jsonParams.put("rate", 0);
                    }
                    else {
                        jsonParams.put("rate", stars);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonParams,
                        response -> {
                            try {
                                boolean error = response.getBoolean("error");
                                String mssg = response.getString("msg");
                                Log.d("FeedbackResponse", response.toString());

                                if (error) {
                                    Toast.makeText(feedback.this, mssg, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(feedback.this, mssg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> Toast.makeText(feedback.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                );

                queue.add(request);



                Intent intent = new Intent(feedback.this,dashboard.class);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        // Set click listeners for stars
        for (int i = 0; i < stars.length; i++) {
            int finalI = i;
            stars[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentRating == finalI + 1) {
                        // If the same star is clicked again, clear all stars
                        updateStars(0);
                    } else {
                        // Otherwise, set the rating to the clicked star
                        updateStars(finalI + 1);
                    }
                }
            });
        }



    }

    private void updateStars(int rating) {
        currentRating = rating;
        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageDrawable(getResources().getDrawable(R.drawable.starnew, getApplicationContext().getTheme()));
            } else {
                stars[i].setImageDrawable(getResources().getDrawable(R.drawable.star_notfill, getApplicationContext().getTheme()));
            }
        }
        Log.d("Stars", "Current Rating: " + currentRating);
    }

}
