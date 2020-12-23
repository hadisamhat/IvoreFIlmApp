package com.example.ivorefilmapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MovieDetails extends AppCompatActivity {
    private  String baseUrl ;
    ImageView movieImage;
    TextView movieName;
    TextView movieDes;
    Button play;
    Button addToList;

    String mName,mImage,mDescription,mId,token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        baseUrl = getResources().getString(R.string.url);
        handleSSLHandshake();
        movieImage = findViewById(R.id.movieImage);
        movieName = findViewById(R.id.movie_title);
        play = findViewById(R.id.play);
        addToList = findViewById(R.id.add_movie_to_list);
        movieDes = findViewById(R.id.description);
        mId = getIntent().getStringExtra("movieId");
        mName = getIntent().getStringExtra("movieName");
        mImage = getIntent().getStringExtra("movieImage");
        mDescription = getIntent().getStringExtra("movieDescription");
        token = getIntent().getStringExtra("accessToken");
        if(getIntent().hasExtra("List")){
            Glide.with(this).load(mImage).into(movieImage);
            movieName.setText(mName.toUpperCase());
            movieDes.setText(mDescription);
            addToList.setText("Remove");
            addToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveFromList(token,mName,mId);
                    Intent sendIntent = new Intent(MovieDetails.this,MainActivity.class);
                    sendIntent.putExtra("accessToken", token);

                    setResult(MovieDetails.RESULT_OK, sendIntent);
                    startActivity(sendIntent);
                }
            });
        }
        else{
        Glide.with(this).load(mImage).into(movieImage);
        movieName.setText(mName.toUpperCase());
        movieDes.setText(mDescription);

        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList.setFocusable(false);
                AddToList(token,mName,mId);
            }
        });}
    }

    public void AddToList(final String token, final String movieName, String mId){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(MovieDetails.this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);
            jsonBody.put("movieId", Integer.valueOf(mId));
            final String mRequestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    baseUrl.concat("/api/WatchList/AddMovieToList()"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    showToast(movieName + " Added To List");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("this is the link ",baseUrl.concat("/api/WatchList/AddMovieToList()"));
                    showToast("Something Went Wrong");

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }


                    @Override
                    public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + token);
                    return params;
                }


            };
            requestQueue.add(stringRequest);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void RemoveFromList(final String token, final String movieName, String mId){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(MovieDetails.this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);
            jsonBody.put("movieId", Integer.valueOf(mId));
            final String mRequestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    baseUrl.concat("/api/WatchList/RemoveMovieFromList()"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    showToast(movieName + " Removed From List");

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("this is the link ",baseUrl.concat("/api/WatchList/RemoveMovieFromList()"));
                    showToast("Something Went Wrong");

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer "+ token);
                    return params;
                }


            };
            requestQueue.add(stringRequest);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MovieDetails.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}