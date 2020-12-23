package com.example.ivorefilmapp;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayDeque;
import java.util.Deque;

public class MainActivity extends AppCompatActivity {

    String token,refreshToken;
    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(4);
    boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        integerDeque.push(R.id.btn_home);
        token = getIntent().getStringExtra("accessToken");
        refreshToken = getIntent().getStringExtra("refreshToken");
        loadFragment(new HomeFragment());

        bottomNavigationView.setSelectedItemId(R.id.btn_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                int id = item.getItemId();

                if(integerDeque.contains(id)){
                    if(id == R.id.btn_home){
                        if(integerDeque.size() != 1){
                            if(flag){
                                integerDeque.addFirst(R.id.btn_home);
                                flag = false;
                            }
                        }
                    }
                    integerDeque.remove(id);
                }
                integerDeque.push(id);
                loadFragment(getFragment(item.getItemId()));
                return true;
            }
        });

    }


    public static String getArgument(Activity activity) {
        return activity.getIntent().getStringExtra("accessToken");
    }
    private Fragment getFragment(int itemId) {
        switch (itemId){
            case R.id.btn_home:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return  new HomeFragment();
            case R.id.btn_search:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return  new SearchFragment();
            case R.id.btn_list:
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return  new ListFragment();
            case R.id.btn_more:
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                return  new MoreFragment();
        }
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        return new HomeFragment();
    }

    private void loadFragment(Fragment fragment) {
        Bundle args = new Bundle();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,fragment,fragment.getClass().getSimpleName())
                .commit();

        args.putString("accessToken", token);
        args.putString("refreshToken", refreshToken);
        fragment.setArguments(args);
    }

    @Override
    public void onBackPressed() {
        integerDeque.pop();

        if(!integerDeque.isEmpty()){
            loadFragment(getFragment(integerDeque.peek()));
        }
        else{
            finish();
        }
    }

}