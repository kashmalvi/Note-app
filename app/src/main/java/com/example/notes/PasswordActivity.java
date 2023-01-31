package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class PasswordActivity extends AppCompatActivity {

    FloatingActionButton addpassButton;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    PasswordAdaptor passAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        addpassButton = findViewById(R.id.add_pass_btn);
        recyclerView = findViewById(R.id.pass_recycler_view);
        menuBtn = findViewById(R.id.pass_menu_btn);

        addpassButton.setOnClickListener(v -> startActivity(new Intent(PasswordActivity.this, PasswordDetailActivity.class)));
        menuBtn.setOnClickListener(v -> showMenu());
        setupRecyclerView();
    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForPassword().orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Password> option = new FirestoreRecyclerOptions.Builder<Password>()
                .setQuery(query, Password.class).build();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        passAdapter = new PasswordAdaptor(option, this);
        recyclerView.setAdapter(passAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        passAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        passAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        passAdapter.notifyDataSetChanged(); //after adding the note recycler view show be updated and displayed
    }

    void showMenu() {
        //to display menu
        PopupMenu popupMenu = new PopupMenu(PasswordActivity.this, menuBtn);
        popupMenu.getMenu().add("LogOut");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle() == "LogOut"){
                    //make logout from firebase firebaseAuth
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}