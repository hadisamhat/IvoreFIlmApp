package com.example.ivorefilmapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ivorefilmapp.Models.BannerMovies;
import com.example.ivorefilmapp.Models.BannerMoviesPagerAdapter;
import com.example.ivorefilmapp.Models.CategoryItem;
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
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String token;
    private String baseUrl;
    private static List<CategoryItem> categoryItemList;
    ArrayAdapter<String> adapter;
    AutoCompleteTextView editText;
    RecyclerView recyclerView;
    View v;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
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
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        v = inflater.inflate(R.layout.fragment_search, container, false);

        baseUrl = getResources().getString(R.string.url);
        Bundle args = getArguments();
        this.token = args.getString("accessToken");
        handleSSLHandshake();
        editText = v.findViewById(R.id.search_item);
        getName(v,editText);
        getSearchItem(v,editText);

        return v;
    }

    private void getSearchItem(final View view, final EditText editText){


        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(final View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    final RequestQueue queue = Volley.newRequestQueue(v.getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/Search/Search?name=").concat(editText.getText().toString()),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject data = new JSONObject(response);
                                        categoryItemList = new ArrayList<>();
                                        if(data.has("MovieId")){

                                            categoryItemList.add(new CategoryItem(data.getString("MovieId"),data.getString("Description"),"movie",data.getString("Name"),baseUrl.concat("/api/Movie/GetThumbnail?movieId=").concat(data.getString("MovieId")),""));
                                        }else if(data.has("seriesName")) {
                                            categoryItemList.add(new CategoryItem(data.getString("Id"),data.getString("SeriesDescription"),"serie",data.getString("SeriesName"),baseUrl.concat("/api/Series/GetSeriesThumbnail?seriesId=").concat(data.getString("Id")),""));
                                        }
                                        setRecycler(view,categoryItemList);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(v.getContext(), "No Result Match Name Given", Toast.LENGTH_SHORT).show();
                            Log.i("Error", "Something Wrong Happened");
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
                    return true;
                }
                return  false;
            }
        });


    }
    private void getName(final View v, final AutoCompleteTextView editText){


        final RequestQueue queue = Volley.newRequestQueue(v.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/Search/GetNames").concat(editText.getText().toString()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);

                            List<String> list = new ArrayList<String>();
                            for(int i = 0; i < arr.length(); i++){
                                list.add(arr.getString(i));
                            }
                            adapter = new ArrayAdapter<String>
                                    (v.getContext(),android.R.layout.select_dialog_item,  list);
                            AutoCompleteTextView actv =  (AutoCompleteTextView) editText;
                            actv.setThreshold(1);//will start working from first character
                            actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(v.getContext(), "No Result Match Name Given", Toast.LENGTH_SHORT).show();
                Log.i("Error", "Something Wrong Happened");
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
    public void setRecycler(final View v ,List<CategoryItem> allCategoryList){
        recyclerView = v.findViewById(R.id.search_bar);
        ListItemRecycler itemRecyclerAdapter = new ListItemRecycler(v.getContext(),allCategoryList,this.token,0);
        int spanCount = 2; // 3 columns
        int spacing = 50; // 50px

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