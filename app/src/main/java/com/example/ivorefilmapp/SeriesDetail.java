package com.example.ivorefilmapp;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.example.ivorefilmapp.Models.CategoryItem;
import com.example.ivorefilmapp.Models.EpisodeAdapter;
import com.example.ivorefilmapp.Models.Series;
import com.example.ivorefilmapp.databinding.LayoutEpisodesBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeriesDetail extends AppCompatActivity {
    private static  String baseUrl ;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBinding layoutEpisodesBinding;
    ImageView serieImage;
    TextView seriesName;
    TextView seriesDes;
    Button getEpisodes;
    Button addToList;
    List<Series> seriesList;
    String sName,sImage,sDescription,sId,token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);
        baseUrl = getResources().getString(R.string.url);

        serieImage = findViewById(R.id.seriesImage);
        seriesName = findViewById(R.id.series_title);
        getEpisodes = findViewById(R.id.getEpisodes);
        addToList = findViewById(R.id.addSeriesToList);
        seriesDes = findViewById(R.id.series_description);

        sName = getIntent().getStringExtra("serieName");
        sImage = getIntent().getStringExtra("serieImage");
        sDescription = getIntent().getStringExtra("serieDescription");
        sId = getIntent().getStringExtra("serieId");
        token = getIntent().getStringExtra("accessToken");

        if(getIntent().hasExtra("List")){
            Glide.with(this).load(sImage).into(serieImage);
            seriesName.setText(sName.toUpperCase());
            seriesDes.setText(sDescription);
            addToList.setText("Remove");
            addToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveFromList(token,sName,sId);
                    Intent sendIntent = new Intent(SeriesDetail.this,MainActivity.class);
                    sendIntent.putExtra("accessToken", token);

                    setResult(SeriesDetail.RESULT_OK, sendIntent);
                    startActivity(sendIntent);
                }
            });
        }
        else{
            Glide.with(this).load(sImage).into(serieImage);
            seriesName.setText(sName.toUpperCase());
            seriesDes.setText(sDescription);

            addToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToList.setFocusable(false);
                    AddToList(token,sName,sId);
                }
            });
        }


        getEpisodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getApiEpisode(sId);
            }
        });




    }


    public void getApiEpisode(String sId){
        RequestQueue queue = Volley.newRequestQueue(SeriesDetail.this);
        StringRequest newRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/Series/GetEpisodes?serieId=").concat(sId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray list = new JSONArray(response);
                            seriesList = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject obj = list.getJSONObject(i);
                                Log.i("dattta",obj.toString());
                                seriesList.add(new Series(obj.getString("EpisodeName"),obj.getString("EpisodeLength")));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addrecycler(seriesList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }}) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer "+ token);
                return params;
            }
        };
        queue.add(newRequest);

    }

    public void addrecycler(List<Series> series){

            episodesBottomSheetDialog = new BottomSheetDialog(SeriesDetail.this);
            layoutEpisodesBinding = DataBindingUtil.inflate(LayoutInflater.from(SeriesDetail.this),
                    R.layout.layout_episodes,
                    (ViewGroup) findViewById(R.id.episodesContainer),
                    false
            );
            episodesBottomSheetDialog.setContentView(layoutEpisodesBinding.getRoot());
            layoutEpisodesBinding.episodesRecyclerView.setAdapter(
                    new EpisodeAdapter(SeriesDetail.this,series,token)

            );
            layoutEpisodesBinding.imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    episodesBottomSheetDialog.dismiss();
                }
            });

            episodesBottomSheetDialog.show();

    }
    public void AddToList(final String token, final String seriesName, String sId){
        try {

            final RequestQueue requestQueue = Volley.newRequestQueue(SeriesDetail.this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);
            jsonBody.put("seriesId", Integer.valueOf(sId));
            final String mRequestBody = jsonBody.toString();
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    baseUrl.concat("/api/WatchList/AddSerieToList()"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    showToast(seriesName + " Added To List");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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
    public void RemoveFromList(final String token, final String sName, String sId){
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(SeriesDetail.this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);
            jsonBody.put("seriesId", Integer.valueOf(sId));
            final String mRequestBody = jsonBody.toString();
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    baseUrl.concat("/api/WatchList/RemoveSerieFromList()"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    showToast(sName + " Removed From List");

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
                Toast.makeText(SeriesDetail.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}