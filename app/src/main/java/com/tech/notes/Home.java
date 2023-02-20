package com.tech.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LinearLayout note_btn = findViewById(R.id.btn_notes);
        LinearLayout password_btn = findViewById(R.id.btn_passwords);
        ImageButton logOut = findViewById(R.id.logout);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, LoginActivity.class));
                finish();
            }
        });

        note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, MainActivity.class));
            }
        });

        password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, PasswordActivity.class));
            }
        });
    }

}
