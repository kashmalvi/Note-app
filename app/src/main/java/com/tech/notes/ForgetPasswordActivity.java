package com.tech.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText email ;
    TextView login;
    MaterialButton resetPasswordButton;
    FirebaseAuth auth;
    String e_mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.forget_email_edit_text);
        login = findViewById(R.id.back_to_login);
        resetPasswordButton = findViewById(R.id.reset_password_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    private void validate() {
        e_mail = email.getText().toString();
        if(e_mail.isEmpty()){
            email.setError("Email Required");
        }
        else{
            auth.sendPasswordResetEmail(e_mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Utility.showToast(ForgetPasswordActivity.this, "Check Your Email ");
                        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                        finish();
                    }else{
                        Utility.showToast(ForgetPasswordActivity.this, task.getException().getMessage());
                        finish();
                    }
                }
            });
        }
    }
}