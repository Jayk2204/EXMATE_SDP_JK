package com.example.exmate_sdp.views.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exmate_sdp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupName, signupCPassword;
    private Button signupButton;



    protected void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth=FirebaseAuth.getInstance();
        signupEmail=findViewById(R.id.signup_email);
        signupPassword=findViewById(R.id.signup_password);
        signupButton=findViewById(R.id.signup_button);

        signupButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user=signupEmail.getText().toString().trim();
                        String pass=signupPassword.getText().toString().trim();

                        if(user.isEmpty())
                        {
                            signupEmail.setError("Enter Email");
                            signupEmail.requestFocus();
                        }
                        if (pass.isEmpty())
                        {
                            signupPassword.setError("Enter Password");
                            signupPassword.requestFocus();
                        }
                       else
                        {
                            auth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(SignUpActivity.this,"User Not Created",Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });

                        }

                    }
                }
        );

    }
}
