package com.example.ivorefilmapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ivorefilmapp.Models.AllCategory;
import com.example.ivorefilmapp.Models.BannerMovies;
import com.example.ivorefilmapp.Models.BannerMoviesPagerAdapter;
import com.example.ivorefilmapp.Models.CategoryItem;
import com.google.android.material.tabs.TabLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String baseUrl;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MainRecyclerAdapter mainRecyclerAdapter;
    RecyclerView mainRecycler;
    String token,seriesCategory,moviesCategory;
    View v;
    BannerMoviesPagerAdapter bannerMoviesPagerAdapter;
    TabLayout indicatorTab,categoryTab;
    ViewPager viewPager;

    List<AllCategory> movies,series;
    List<BannerMovies> movieBannerList;
    List<BannerMovies> seriesBannerList;
    List<CategoryItem> movieCatItemList,serieCatItemList;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {

        HomeFragment fragment = new HomeFragment();
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
    public static String
    removeFirstandLast(String str)
    {

        // Removing first and last character
        // of a string using substring() method
        str = str.substring(1, str.length() - 1);

        // Return the modified string
        return str;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        token = args.getString("accessToken");
        v = inflater.inflate(R.layout.fragment_home, container, false);
        baseUrl = getResources().getString(R.string.url);


        handleSSLHandshake();
        indicatorTab = v.findViewById(R.id.tab_indicator);
        categoryTab = v.findViewById(R.id.tabLayout);





        movieBannerList = new ArrayList<>();
        movieBannerList.add(new BannerMovies(10,"The Darkest Knight","movie","movie",baseUrl.concat("/api/Movie/GetThumbnail?movieId=10"),""));
        movieBannerList.add(new BannerMovies(11,"Catch Me If You Can","movie","movie",baseUrl.concat("/api/Movie/GetThumbnail?movieId=11"),""));
        movieBannerList.add(new BannerMovies(12,"Shutter Island","movie","movie",baseUrl.concat("/api/Movie/GetThumbnail?movieId=12"),""));





        seriesBannerList = new ArrayList<>();
        seriesBannerList.add(new BannerMovies(5,"The Queen's Gambit","serie","serie",baseUrl.concat("/api/Series/GetSeriesThumbnail?seriesId=5"),""));
        seriesBannerList.add(new BannerMovies(6,"Black Mirror","serie","serie",baseUrl.concat("/api/Series/GetSeriesThumbnail?seriesId=6"),""));


        setBannerMoviesPagerAdapter(v,movieBannerList);


        categoryTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:

                        setBannerMoviesPagerAdapter(v,movieBannerList);

                        return;
                    case 1:
                        setBannerSeriesPagerAdapter(v,seriesBannerList);
                        return;

                    default:
                        setNullAdapter(v);
                        return;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return v;

    }
    private void setMoviesCategory(final View v){

        movies = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/Movie/GetMoviesCategrories"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        moviesCategory = removeFirstandLast(response);
                        for (String categoryTab:  moviesCategory.split(","))
                        {

                            movieCatItemList = new ArrayList<>();
                            String cat = categoryTab.toUpperCase().replaceAll("\"","");

                            getMoviesByCat(v,cat,movieCatItemList,movies);


                        };
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
    private void setSeriesCategory(final View v){

        series = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/Series/GetSeriesCategrories"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        seriesCategory = removeFirstandLast(response);

                        for (String categoryTab:  seriesCategory.split(","))
                        {

                            serieCatItemList = new ArrayList<>();
                            String cat = categoryTab.toUpperCase().replaceAll("\"","");

                            getSeriesBycat(v,cat,serieCatItemList,series);
                        };
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
    private void getSeriesBycat(final View v,final String cat, final List<CategoryItem> seriesList, final List<AllCategory> series){
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/Series/GetSeriesByCategory/").concat(cat.toLowerCase()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray reader = new JSONArray(response);
                            for (int i = 0; i < reader.length(); i++) {
                                JSONObject obj = reader.getJSONObject(i);
                                seriesList.add(new CategoryItem(obj.getString("Id"),obj.getString("SeriesDescription"),"serie",obj.getString("SeriesName"),baseUrl.concat("/api/Series/GetSeriesThumbnail?seriesId=").concat(obj.getString("Id")),""));


                            }
                            series.add(new AllCategory(cat.toUpperCase().replaceAll("\"",""),seriesList));
                            setMainRecycler(v,series);
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
    private void getMoviesByCat(final View v,final String cat, final List<CategoryItem> movieList, final List<AllCategory> movies){

        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/Movie/GetMoviesByCategory/").concat(cat.toLowerCase()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray reader = new JSONArray(response);
                            for (int i = 0; i < reader.length(); i++) {
                                JSONObject obj = reader.getJSONObject(i);

                                movieList.add(new CategoryItem(obj.getString("MovieId"),obj.getString("Description"),"movie",obj.getString("Name"),baseUrl.concat("/api/Movie/GetThumbnail?movieId=").concat(obj.getString("MovieId")),""));


                            }
                            movies.add(new AllCategory(cat.toUpperCase().replaceAll("\"",""),movieList));
                            setMainRecycler(v,movies);
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

    private void setBannerMoviesPagerAdapter(View v,List<BannerMovies> bannerMoviesList){
        viewPager = v.findViewById(R.id.banner_viewPager);
        bannerMoviesPagerAdapter = new BannerMoviesPagerAdapter(this.getContext(),bannerMoviesList,token);
        viewPager.setAdapter(bannerMoviesPagerAdapter);
        indicatorTab.setBackgroundResource(0);
        indicatorTab.setupWithViewPager(viewPager);
        setMoviesCategory(v);




    }

    private void setBannerSeriesPagerAdapter(View v,List<BannerMovies> bannerMoviesList){
        viewPager = v.findViewById(R.id.banner_viewPager);
        bannerMoviesPagerAdapter = new BannerMoviesPagerAdapter(this.getContext(),bannerMoviesList,token);
        viewPager.setAdapter(bannerMoviesPagerAdapter);
        indicatorTab.setBackgroundResource(0);
        indicatorTab.setupWithViewPager(viewPager);
        setSeriesCategory(v);



    }

    private void setNullAdapter(View v){
        viewPager = v.findViewById(R.id.banner_viewPager);
        viewPager.setAdapter(null);
        indicatorTab.setBackgroundResource(0);
        indicatorTab.setupWithViewPager(viewPager);



    }

    public void setMainRecycler(final View v ,List<AllCategory> allCategoryList){
        mainRecycler = v.findViewById(R.id.main_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext(),RecyclerView.VERTICAL,false);
        mainRecycler.setLayoutManager(layoutManager);
        mainRecyclerAdapter = new MainRecyclerAdapter(v.getContext(),allCategoryList,this.token);
        mainRecycler.setAdapter(mainRecyclerAdapter);

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
