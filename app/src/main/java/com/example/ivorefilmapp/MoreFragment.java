package com.example.ivorefilmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ivorefilmapp.Models.CategoryItem;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static  String baseUrl;
    String token,refreshToken;
    Element logout,versionElement,nameElement;
    private Session session;
    private SharedPreferences prefs;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        baseUrl = getResources().getString(R.string.url);
        session = new Session(container.getContext());
        versionElement = new Element();
        versionElement.setTitle("Version 0.1");
        Bundle args = getArguments();
        token = args.getString("accessToken");
        refreshToken = args.getString("refreshToken");
        logout = new Element();
        logout.setTitle("Logout");
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    RequestQueue requestQueue = Volley.newRequestQueue(container.getContext());
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("token", token);
                    jsonBody.put("refreshToken", refreshToken);
                    final String mRequestBody = jsonBody.toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            baseUrl.concat("/api/User/Logout()"), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Intent sendIntent = new Intent(container.getContext(), LoginActivity.class);
                            startActivity(sendIntent);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(container.getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();

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


                    };
                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        nameElement = new Element();
        GetName(container,token);
        nameElement.setIconDrawable(R.drawable.ic_baseline_account_circle_24);

        return new AboutPage(getContext())
                .isRTL(false)
                .enableDarkMode(false)
                .addItem(nameElement)
                .setDescription(getString(R.string.app_description))
                .addEmail("hadisamhat@hotmail.com", "Email")
                .addFacebook("hadi samhat")
                .addTwitter("Hadisamhat9")
                .addGitHub("hadisamhat")
                .addInstagram("hadisam1")
                .addItem(versionElement)
                .addItem(logout)
                .create();
    }

    public void GetName(final ViewGroup v, final String token) {
        final RequestQueue queue = Volley.newRequestQueue(v.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl.concat("/api/User/UserProfile?token=").concat(token),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String name = null;
                        try {
                            JSONObject data = new JSONObject(response);
                            name = data.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SetUserName(name);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(v.getContext(), "No Result Match Name Given", Toast.LENGTH_SHORT).show();
                Log.i("Error", "Something Wrong Happened");
            }
        });
        queue.add(stringRequest);
    }

    public void SetUserName(String name){
        nameElement.setTitle(name);
   };
/*
    public void AddAboutPage(String fullname){
         new AboutPage(getContext())
                .isRTL(false)
                .enableDarkMode(false)
                .setDescription(getString(R.string.app_description))
                .addEmail("hadisamhat@hotmail.com", "Email")
                .addFacebook("hadi samhat")
                .addTwitter("Hadisamhat9")
                .addGitHub("hadisamhat")
                .addInstagram("hadisam1")
                .addItem(versionElement)
                .addItem(logout)
                .create();
    }
*/
    
}