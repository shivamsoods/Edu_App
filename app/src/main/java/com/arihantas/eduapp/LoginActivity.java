package com.arihantas.eduapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton btnGoogleSignIn;
    private Button btnEmailLogin, btnEmailSubmit, btnGoBack;
    private LinearLayout llEmailLogin;
    private RelativeLayout rlLoginType;
    private EditText etLoginEmail, etPasswordEmail;
    private TextView tvEmailSignUp;
    private boolean emailLoginBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        btnGoogleSignIn = findViewById(R.id.btn_google_sign_in);

        btnEmailLogin = findViewById(R.id.btn_email_login);
        btnEmailSubmit = findViewById(R.id.btn_email_login_submit);
        llEmailLogin = findViewById(R.id.ll_login_email);
        etLoginEmail = findViewById(R.id.et_login_email);
        etPasswordEmail = findViewById(R.id.et_login_password);
        tvEmailSignUp = findViewById(R.id.tv_email_signup);
        rlLoginType = findViewById(R.id.rl_login_type);
        btnGoBack = findViewById(R.id.btn_email_back);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("612547119555-b5j2i97rjmh5pr7s6513uevvl2g3s8le.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        btnEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEmailLogin.setVisibility(View.VISIBLE);
                rlLoginType.setVisibility(View.GONE);
                emailLoginBool = true;

                if (emailLoginBool) {
                    btnEmailSubmit.setText("Log In");
                }
            }
        });

        btnEmailSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){
                if (emailLoginBool) {
                    emailLogIn();
                } else {
                    emailSignUp();
                }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvEmailSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEmailLogin.setVisibility(View.VISIBLE);
                rlLoginType.setVisibility(View.GONE);
                emailLoginBool = false;

                if (!emailLoginBool) {
                    btnEmailSubmit.setText("Sign Up");
                }
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoginType.setVisibility(View.VISIBLE);
                llEmailLogin.setVisibility(View.GONE);
            }
        });
    }

    private boolean check(){
        if(etLoginEmail.getText().toString().trim().isEmpty() ||etPasswordEmail.getText().toString().trim().isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }
    private void emailSignUp() {
        mAuth.createUserWithEmailAndPassword(etLoginEmail.getText().toString().trim(), etPasswordEmail.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        Toast.makeText(this, "Sign in with " + etLoginEmail.getText().toString().trim() + " and pass " + etPasswordEmail.getText().toString().trim(), Toast.LENGTH_SHORT).show();
    }

    private void emailLogIn() {
        mAuth.signInWithEmailAndPassword(etLoginEmail.getText().toString().trim(), etPasswordEmail.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        Toast.makeText(this, "Sign in with " + etLoginEmail.getText().toString().trim() + " and pass " + etPasswordEmail.getText().toString().trim(), Toast.LENGTH_SHORT).show();
    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Could not login IN", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }
}

