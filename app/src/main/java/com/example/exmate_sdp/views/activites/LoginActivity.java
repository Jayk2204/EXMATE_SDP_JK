package com.example.exmate_sdp.views.activites;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exmate_sdp.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private Dialog loader; // premium loader

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_Button);

        setupLoader();

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void setupLoader() {
        loader = new Dialog(this);
        loader.setContentView(R.layout.exmate_loader);
        loader.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loader.getWindow().getAttributes().windowAnimations = R.style.LoaderAnimation;
        loader.setCancelable(false);
    }

    private void loginUser() {
        String email = loginEmail.getText().toString().trim();
        String pass = loginPassword.getText().toString().trim();

        // -------------------------
        // VALIDATION
        // -------------------------

        if (email.isEmpty()) {
            loginEmail.setError("Enter email");
            loginEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Invalid email format");
            loginEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            loginPassword.setError("Enter password");
            loginPassword.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            loginPassword.setError("Minimum 6 characters");
            loginPassword.requestFocus();
            return;
        }

        // -------------------------
        // SHOW PREMIUM LOADER
        // -------------------------
        loader.show();

        // -------------------------
        // FIREBASE LOGIN
        // -------------------------

        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {

                    loader.dismiss();

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
