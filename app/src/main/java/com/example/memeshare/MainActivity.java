 package com.example.memeshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

 public class MainActivity extends AppCompatActivity {

     ImageView img;
     Button btnnxt,btnshare;
     private String currentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        img=findViewById(R.id.img);
        btnnxt=findViewById(R.id.btnnxt);
        btnshare=findViewById(R.id.btnshare);
        loadMeme();
        btnnxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMeme();
            }
        });
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMeme();
            }
        });
    }
    private  void loadMeme(){

        String url = "https://meme-api.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the API response
                        try {
                            currentUrl = response.getString("url");
                            // Use Glide to load the image
                            Glide.with(MainActivity.this)
                                    .load(currentUrl)
                                    .into(img);
                        } catch (JSONException e) {
                           e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Failed to parse response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        // Handle the error
                    }
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }
    private void shareMeme(){
        if (currentUrl != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this meme: " + currentUrl);
            startActivity(Intent.createChooser(shareIntent, "Share this meme using"));
        } else {
            Toast.makeText(this, "No meme to share!", Toast.LENGTH_SHORT).show();
        }
    }   
}