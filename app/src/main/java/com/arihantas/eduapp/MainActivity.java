package com.arihantas.eduapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public  class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        loadFragment(new StudyFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation_bottom = findViewById(R.id.bottom_navigation_view);
        navigation_bottom.setOnNavigationItemSelectedListener(this);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_bottom_study:
                fragment = new StudyFragment();
                Toast.makeText(getApplicationContext(), "Study", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navigation_bottom_test:
                fragment = new TestFragment();
                Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navigation_bottom_challenge:
                fragment = new ChallengeFragment();
                Toast.makeText(getApplicationContext(), "Challenge", Toast.LENGTH_SHORT).show();
                break;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.navigation_top_profile:
                //startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                Toast.makeText(getApplicationContext(), "PROFILE", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_top_feedback:
                //startActivity(new Intent(MainActivity.this,FeedbackActivity.class));
                Toast.makeText(getApplicationContext(), "FEEDBACK", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigatioan_top_sign_out:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;


        }
        return true;

    }
}