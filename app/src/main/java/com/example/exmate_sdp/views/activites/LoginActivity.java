package com.example.exmate_sdp.views.activites;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Patterns;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exmate_sdp.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private Dialog loader;
    private TextView signupRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_Button);
        signupRedirect = findViewById(R.id.signupredirecttext);

        setupLoader();
        applyGradient(signupRedirect); // ⭐ Gradient text applied

        loginButton.setOnClickListener(v -> loginUser());

        // ----------------------------------------------------------
        // PREMIUM SIGNUP REDIRECT (pulse + fade + slide transition)
        // ----------------------------------------------------------

        signupRedirect.setOnClickListener(v -> {

            // Pulse bounce animation
            signupRedirect.startAnimation(
                    AnimationUtils.loadAnimation(LoginActivity.this, R.anim.pulse)
            );

            // Fade effect
            signupRedirect.animate()
                    .alpha(0.6f)
                    .setDuration(120)
                    .withEndAction(() ->
                            signupRedirect.animate().alpha(1f).setDuration(120)
                    );

            // Slide transition to SignupActivity
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }

    // ------------------------------------------------------------
    // ⭐ APPLY GRADIENT TO SIGNUP TEXT
    // ------------------------------------------------------------
    private void applyGradient(TextView textView) {
        textView.post(() -> {
            Shader shader = new LinearGradient(
                    0, 0, textView.getWidth(), textView.getHeight(),
                    new int[]{0xFFFF6F00, 0xFFFFA040},
                    null,
                    Shader.TileMode.CLAMP
            );
            textView.getPaint().setShader(shader);
        });
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

        loader.show();

        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    loader.dismiss();
                    if (task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
