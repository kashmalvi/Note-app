package com.tech.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtn = findViewById(R.id.create_button_btn);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(v -> createAccount());
        loginBtnTextView.setOnClickListener(v -> finish());

    }
    void createAccount(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);
        if(!isValidated)
            return;

        //if validation is true then we will create firebase authentication
        createAccountInFirebase(email, password);

    }

    void createAccountInFirebase(String email, String password){
        changeInProgress(true);

        //to create account in firebase we have class Auth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            //creation of account is done
                            Utility.showToast(CreateAccountActivity.this,"Account Created,Check Email to Verify");
                            //firebase return the current user and make them signIn  only after email verification
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }
                        else{
                            //creation of account is failed
                            Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                }
        );
    }

    //while taking into action create account let show a progressbar
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    //after taking input from user in create account lets validate it
    boolean validateData(String email, String password, String confirmPassword){

        //using patter class
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is Invalid");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password length is too Short");
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Password not match");
            return false;
        }
        return true;
    }
}