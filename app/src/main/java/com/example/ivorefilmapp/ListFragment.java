package com.example.ivorefilmapp;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ivorefilmapp.Models.AllCategory;
import com.example.ivorefilmapp.Models.CategoryItem;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String token;
    private String baseUrl;
    private static List<CategoryItem> categoryItemList;

    RecyclerView recyclerView;
    View v;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseUrl = getResources().getString(R.string.url);
        Bundle args = getArguments();
        this.token = args.getString("accessToken");
        handleSSLHandshake();
        v = inflater.inflate(R.layout.fragment_list, container, false);

        getListItem(v);

        return v;
    }
    private void getListItem(final View v){

        final RequestQueue queue = Volley.newRequestQueue(v.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/WatchList/GetList?token=").concat(token),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response).getJSONObject("Result");
                            JSONArray list = data.getJSONArray("ListViewModels");
                            categoryItemList = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject obj = list.getJSONObject(i);
                                if(obj.has("movieName")){

                                    getMovieList(v,categoryItemList,obj.getString("movieName"));
                                }else if(obj.has("seriesName")) {
                                    getShowList(v,categoryItemList,obj.getString("seriesName"));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error","Something Wrong Happened");
            }}) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer "+ token);
                return params;
            }

        };
        queue.add(stringRequest);

    }
    public void getMovieList(final View v, final List<CategoryItem> movieList, String MovieName){
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        StringRequest newRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/Movie/GetMovieByName?movieName=").concat(MovieName.replace(" ","%20")),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            movieList.add(new CategoryItem(obj.getString("MovieId"),obj.getString("Description"),"movie",obj.getString("Name"),baseUrl.concat("/api/Movie/GetThumbnail?movieId=").concat(obj.getString("MovieId")),""));
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                        setRecycler(v,movieList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("THIS IS THE URLLLLL",baseUrl.concat("/api/Movie/GetMovieByName?movieName="));
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
    public void getShowList(final View v, final List<CategoryItem> showList, String ShowName){
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        StringRequest newRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/Series/GetSeriesByname?serieName=").concat(ShowName.replace(" ","%20")),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            showList.add(new CategoryItem(obj.getString("Id"),obj.getString("SeriesDescription"),"serie",obj.getString("SeriesName"),baseUrl.concat("/api/Series/GetSeriesThumbnail?seriesId=").concat(obj.getString("Id")),""));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setRecycler(v,showList);
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
    public void setRecycler(final View v ,List<CategoryItem> allCategoryList){
        recyclerView = v.findViewById(R.id.item_list);
        ListItemRecycler itemRecyclerAdapter = new ListItemRecycler(v.getContext(),allCategoryList,this.token,1);
        int spanCount = 2; // 3 columns
        int spacing = 30; // 50px
        boolean includeEdge = true;
        recyclerView.setLayoutManager(new GridLayoutManager(v.getContext(),spanCount));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing));
        recyclerView.setAdapter(itemRecyclerAdapter);

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