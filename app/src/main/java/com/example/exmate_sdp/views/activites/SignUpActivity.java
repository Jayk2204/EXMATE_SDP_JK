package com.example.exmate_sdp.views.activites;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exmate_sdp.R;
import com.example.exmate_sdp.views.animations.ParticleView;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText signupEmail, signupPassword, signupName, signupCPassword;
    private Button signupButton;
    private TextView loginRedirect, appName, tagline, signupTitle;

    private LinearLayout signupCard;
    private RelativeLayout signupRoot;
    private ParticleView particleView;

    private Dialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        // FIND VIEWS
        signupRoot = findViewById(R.id.signupRoot);
        particleView = findViewById(R.id.particleView);

        appName = findViewById(R.id.appName);
        tagline = findViewById(R.id.tagline);

        signupCard = findViewById(R.id.signupCard);
        signupTitle = findViewById(R.id.signupTitle);

        signupName = findViewById(R.id.signup_Name);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupCPassword = findViewById(R.id.signup_cpassword);
        signupButton = findViewById(R.id.signup_button);
        loginRedirect = findViewById(R.id.loginredirecttext);

        setupLoader();
        startIntroAnimations();
        applyGradient(appName);
        applyGradient(signupTitle);
        setup3DCardAnimation();
        setupButtonBounce();

        signupButton.setOnClickListener(v -> registerUser());

        loginRedirect.setOnClickListener(v -> {
            loginRedirect.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse));
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }

    // POPUP (NEW)
    private void showVerificationPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.verification_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        // Auto close after 2 sec
        new android.os.Handler().postDelayed(dialog::dismiss, 2000);
    }

    private void startIntroAnimations() {

        appName.setAlpha(0f);
        appName.setScaleX(0.6f);
        appName.setScaleY(0.6f);
        appName.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(900)
                .setStartDelay(150)
                .start();

        tagline.setAlpha(0f);
        tagline.setTranslationY(20);
        tagline.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(900)
                .setStartDelay(550)
                .start();

        signupCard.setAlpha(0f);
        signupCard.setTranslationY(60);
        signupCard.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(900)
                .setStartDelay(900)
                .start();
    }

    private void applyGradient(TextView tv) {
        tv.post(() -> {
            Shader shader = new LinearGradient(
                    0, 0, tv.getWidth(), tv.getHeight(),
                    new int[]{0xFFFF6F00, 0xFFFFA040},
                    null,
                    Shader.TileMode.CLAMP
            );
            tv.getPaint().setShader(shader);
        });
    }

    private void setup3DCardAnimation() {
        signupCard.setOnTouchListener((v, event) -> {

            float centerX = signupCard.getWidth() / 2f;
            float centerY = signupCard.getHeight() / 2f;

            float dx = event.getX() - centerX;
            float dy = event.getY() - centerY;

            signupCard.setRotationX(dy / 25);
            signupCard.setRotationY(-dx / 25);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                signupCard.animate().rotationX(0).rotationY(0).setDuration(300);
            }
            return true;
        });
    }

    private void setupButtonBounce() {
        signupButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.animate().scaleX(0.92f).scaleY(0.92f).setDuration(120);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.animate().scaleX(1f).scaleY(1f).setDuration(120);
            }
            return false;
        });
    }

    private void setupLoader() {
        loader = new Dialog(this);
        loader.setContentView(R.layout.exmate_loader);
        loader.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loader.setCancelable(false);

        ImageView icon = loader.findViewById(R.id.loaderIcon);
        icon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
    }



    // FIXED SIGNUP (WITH POPUP)
    private void registerUser() {

        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String pass = signupPassword.getText().toString().trim();
        String cpass = signupCPassword.getText().toString().trim();

        // VALIDATION
        if (name.isEmpty()) { signupName.setError("Enter your name"); signupName.requestFocus(); return; }
        if (email.isEmpty()) { signupEmail.setError("Enter your email"); signupEmail.requestFocus(); return; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { signupEmail.setError("Invalid email"); return; }
        if (pass.isEmpty()) { signupPassword.setError("Enter password"); return; }
        if (pass.length() < 6) { signupPassword.setError("Min 6 characters"); return; }
        if (!pass.equals(cpass)) { signupCPassword.setError("Passwords do not match"); return; }

        loader.show();

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        auth.getCurrentUser().sendEmailVerification();

                        loader.dismiss();

                        // SHOW POPUP
                        showVerificationPopup();

                        // Logout & redirect after popup
                        new android.os.Handler().postDelayed(() -> {
                            auth.signOut();
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        }, 2200);

                    } else {

                        loader.dismiss();
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
