package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Random;

public class PasswordDetailActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText, textLength;
    ImageButton savePassBtn;

    Button generate;

    TextView pageTitleTextView; //for edit existing note
    String title, content, docId;
    boolean isEditMode = false;
    TextView deletePassTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_detail);

        titleEditText = findViewById(R.id.pass_title_text);
        contentEditText = findViewById(R.id.pass_content_text);
        savePassBtn = findViewById(R.id.save_password_btn);

        pageTitleTextView = findViewById(R.id.page_title);
        deletePassTextViewBtn = findViewById(R.id.delete_pass_text_view_btn);

        generate = findViewById(R.id.generate);
        textLength = findViewById(R.id.text_length);


        //receiving data from intent made in PasswordAdapter
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("pass");
        docId = getIntent().getStringExtra("docID");

        if(docId != null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);



        if(isEditMode){
            pageTitleTextView.setText("Edit your Password");
            deletePassTextViewBtn.setVisibility(View.VISIBLE);
        }

        savePassBtn.setOnClickListener((v) -> savePassword());
        deletePassTextViewBtn.setOnClickListener((v) -> deletePasswordFromFirebase());

        generate.setOnClickListener(v->{
            int textValue;
            if (textLength.getText().toString().isEmpty()) {
                textValue = 0;
            } else {
                try {
                    textValue = Integer.parseInt(textLength.getText().toString());
                } catch (NumberFormatException e) {
                    // Handle the exception and set a default value if necessary
                    textValue = 0;
                }
            }
            String password = generateRandomPassword(textValue);
            contentEditText.setText(password);
        });



    }
    public String generateRandomPassword(int len) {

        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    private void deletePasswordFromFirebase() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForPassword().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(PasswordDetailActivity.this, "Password Deleted Successfully");
                    finish();
                    //after it will return to main activity
                }else{
                    //note is not added
                    Utility.showToast(PasswordDetailActivity.this, "Failed while deleting Password");
                }
            }
        });
    }

    private void savePassword() {
        String PassAccount = titleEditText.getText().toString();
        String pass = contentEditText.getText().toString();
        if(PassAccount == null || PassAccount.isEmpty()){
            titleEditText.setError("Account is require");
            return;
        }

        Password password = new Password();
        password.setAccount(PassAccount);
        password.setPass(pass);
        password.setTimestamp(Timestamp.now());

        //save the above node-class / note to firebase
        savePasswordToFireBase(password);

    }

    private void savePasswordToFireBase(Password password) {
        DocumentReference documentReference;
        if(isEditMode){
            //update the password
            documentReference = Utility.getCollectionReferenceForPassword().document(docId);
        }
        else{
            //create new password
            documentReference = Utility.getCollectionReferenceForPassword().document();
        }

        documentReference.set(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(PasswordDetailActivity.this, "Password Added Successfully");
                    finish();
                    //after it will return to main activity
                }else{
                    //note is not added
                    Utility.showToast(PasswordDetailActivity.this, "Failed while adding password");
                }
            }
        });
    }
}