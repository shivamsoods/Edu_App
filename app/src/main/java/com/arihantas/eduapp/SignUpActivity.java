package com.arihantas.eduapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private Button btnSubmit;
    private EditText etName, etMobile;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String stEtName, stEtMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        btnSubmit = findViewById(R.id.btn_signup_submit);
        etName = findViewById(R.id.et_signup_name);
        etMobile = findViewById(R.id.et_signup_mobile);


        currentUser = mAuth.getCurrentUser();

        assert currentUser != null;
        if (currentUser.getDisplayName() != null) {
            etName.setText(currentUser.getDisplayName());
            stEtName = currentUser.getDisplayName();
        }
        if (currentUser.getPhoneNumber() != null) {
            etMobile.setText(currentUser.getPhoneNumber());
            stEtMobile = currentUser.getPhoneNumber();
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = fillCheck();
                if (check) {
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill Mobile or Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean fillCheck() {
        stEtName = etName.getText().toString();
        stEtMobile = etMobile.getText().toString();
        if (!stEtName.isEmpty() && !stEtMobile.isEmpty()) {
            if (stEtMobile.length() == 10) {

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
}
