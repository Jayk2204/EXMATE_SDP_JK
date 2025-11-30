package com.example.exmate_sdp.views.activites;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.exmate_sdp.R;
import com.example.exmate_sdp.views.animations.ParticleView;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Dialog loader;

    private EditText email, pass;
    private Button loginBtn;
    private TextView signupText, loginTitle, appName, tagline;

    private LinearLayout loginCard;
    private RelativeLayout loginRoot;

    private ParticleView particleView;   // ⭐ Particle View

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // UI elements
        email = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_Button);
        signupText = findViewById(R.id.signupredirecttext);
        loginTitle = findViewById(R.id.loginTitle);
        loginCard = findViewById(R.id.loginCard);
        loginRoot = findViewById(R.id.loginRoot);

        // Premium App Branding
        appName = findViewById(R.id.appName);
        tagline = findViewById(R.id.tagline);

        // Particle background view
        particleView = findViewById(R.id.particleView);

        setupLoader();

        startPremiumBackground();
        startParticles();           // ⭐ Particle Animation

        animateAppName();           // EXMATE text animation
        animateTagline();           // Tagline fade slide
        applyGradient(appName);
        applyGradient(loginTitle);

        setup3DMotion();
        setupButtonAnimation();

        loginBtn.setOnClickListener(v -> loginUser());

        signupText.setOnClickListener(v -> {
            signupText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse));
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }


    // -----------------------------------------------------------
    // START PARTICLE ANIMATION
    // -----------------------------------------------------------
    private void startParticles() {
        if (particleView != null) {
            particleView.startAnimation();   // Now works because we added in class ✔
        }
    }


    // -----------------------------------------------------------
    // PREMIUM GRADIENT BACKGROUND
    // -----------------------------------------------------------
    private void startPremiumBackground() {
        Drawable bg = ContextCompat.getDrawable(this, R.drawable.premium_bg);

        if (bg instanceof TransitionDrawable) {
            TransitionDrawable transition = (TransitionDrawable) bg;
            loginRoot.setBackground(transition);
            transition.setCrossFadeEnabled(true);
            transition.startTransition(2000);
        } else {
            loginRoot.setBackground(bg);
        }
    }


    // -----------------------------------------------------------
    // Animate EXMATE Logo
    // -----------------------------------------------------------
    private void animateAppName() {

        Animation bubble = AnimationUtils.loadAnimation(this, R.anim.exmate_pop);
        appName.startAnimation(bubble);

        appName.setAlpha(0f);
        appName.setScaleX(0.4f);
        appName.setScaleY(0.4f);

        appName.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(900)
                .setStartDelay(200)
                .start();
    }


    // -----------------------------------------------------------
    // Tagline animation
    // -----------------------------------------------------------
    private void animateTagline() {
        tagline.setAlpha(0f);
        tagline.setTranslationY(20);

        tagline.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(1200)
                .setStartDelay(700)
                .start();
    }


    // -----------------------------------------------------------
    // Gradient Text for Titles
    // -----------------------------------------------------------
    private void applyGradient(TextView text) {
        text.post(() -> {
            Shader shader = new LinearGradient(
                    0, 0, text.getWidth(), text.getHeight(),
                    new int[]{0xFFFF7F00, 0xFFFFC470},
                    null,
                    Shader.TileMode.CLAMP
            );
            text.getPaint().setShader(shader);
        });
    }


    // -----------------------------------------------------------
    // 3D Parallax Motion
    // -----------------------------------------------------------
    private void setup3DMotion() {
        loginCard.setOnTouchListener((v, event) -> {

            float centerX = loginCard.getWidth() / 2f;
            float centerY = loginCard.getHeight() / 2f;

            float deltaX = event.getX() - centerX;
            float deltaY = event.getY() - centerY;

            loginCard.setRotationX(deltaY / 22);
            loginCard.setRotationY(-deltaX / 22);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                loginCard.animate().rotationX(0).rotationY(0).setDuration(250);
            }

            return true;
        });
    }


    // -----------------------------------------------------------
    // Button Click Bounce Animation
    // -----------------------------------------------------------
    private void setupButtonAnimation() {
        loginBtn.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.animate().scaleX(0.92f).scaleY(0.92f).setDuration(120);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.animate().scaleX(1f).scaleY(1f).setDuration(120);
            }
            return false;
        });
    }


    // -----------------------------------------------------------
    // Loader
    // -----------------------------------------------------------
    private void setupLoader() {
        loader = new Dialog(this);
        loader.setContentView(R.layout.exmate_loader);
        loader.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loader.setCancelable(false);
    }


    // -----------------------------------------------------------
    // Firebase Login Logic
    // -----------------------------------------------------------
    private void loginUser() {

        String userEmail = email.getText().toString().trim();
        String userPass = pass.getText().toString().trim();

        if (userEmail.isEmpty()) {
            email.setError("Enter email");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("Invalid email");
            email.requestFocus();
            return;
        }

        if (userPass.isEmpty()) {
            pass.setError("Enter password");
            pass.requestFocus();
            return;
        }

        loader.show();

        auth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(task -> {

                    loader.dismiss();

                    if (task.isSuccessful()) {

                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this, MainActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();

                    } else {
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
