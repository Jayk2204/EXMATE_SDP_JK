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
import com.example.exmate_sdp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupName, signupCPassword;
    private Button signupButton;
    private TextView loginRedirect;
    private Dialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        signupName = findViewById(R.id.signup_Name);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupCPassword = findViewById(R.id.signup_cpassword);
        signupButton = findViewById(R.id.signup_button);
        loginRedirect = findViewById(R.id.loginredirecttext);

        setupLoader();
        applyGradient(loginRedirect);   // ⭐ Add Gradient Text

        signupButton.setOnClickListener(view -> registerUser());

        // ----------------------------------
        // PREMIUM LOGIN REDIRECT ANIMATIONS
        // ----------------------------------

        loginRedirect.setOnClickListener(v -> {

            // Pulse bounce animation
            loginRedirect.startAnimation(
                    AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.pulse)
            );

            // Fade animation
            loginRedirect.animate().alpha(0.6f).setDuration(120).withEndAction(() ->
                    loginRedirect.animate().alpha(1f).setDuration(120)
            );

            // Slide transition
            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }

    // ----------------------------------------------------
    // ⭐ APPLY GRADIENT TEXT (Correct & Professional Way)
    // ----------------------------------------------------
    private void applyGradient(TextView textView) {
        textView.post(() -> {
            Shader shader = new LinearGradient(
                    0, 0, textView.getWidth(), textView.getHeight(),
                    new int[]{0xFFFF6F00, 0xFFFFA040},   // orange → light orange
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

    private void registerUser() {

        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String pass = signupPassword.getText().toString().trim();
        String cpass = signupCPassword.getText().toString().trim();

        // -------------------------
        // VALIDATIONS
        // -------------------------

        if (name.isEmpty()) {
            signupName.setError("Enter your name");
            signupName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            signupEmail.setError("Enter your email");
            signupEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Invalid email format");
            signupEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            signupPassword.setError("Enter password");
            signupPassword.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            signupPassword.setError("Password must be at least 6 characters");
            signupPassword.requestFocus();
            return;
        }

        if (!pass.equals(cpass)) {
            signupCPassword.setError("Passwords do not match");
            signupCPassword.requestFocus();
            return;
        }

        loader.show();

        // -------------------------
        // FIREBASE AUTH SIGNUP
        // -------------------------

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                        // Slide transition to Login
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();

                        // Save user silently
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        User userObj = new User(name, email);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(uid)
                                .setValue(userObj);

                        loader.dismiss();

                    } else {
                        loader.dismiss();
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
