package com.example.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PasswordAdaptor extends FirestoreRecyclerAdapter<Password, PasswordAdaptor.PasswordViewHolder> {

    Context context;

    public PasswordAdaptor(@NonNull FirestoreRecyclerOptions<Password> options, Context context) {
        super(options);
        this.context=context;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onBindViewHolder(@NonNull PasswordViewHolder holder, int position, @NonNull Password password) {
        holder.titleTextView.setText(password.Account);
        holder.timestampTextView.setText(Utility.timestampToString(password.timestamp));
        holder.recyclerPassView.setBackgroundTintList(holder.itemView.getResources().getColorStateList(getRandomColor(), null));


        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, PasswordDetailActivity.class);
            intent.putExtra("title", password.Account);
            intent.putExtra("pass", password.pass);

            String docID = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docID", docID);
            context.startActivity(intent);
        });
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.light01);
        colorCode.add(R.color.light02);
        colorCode.add(R.color.light03);
        colorCode.add(R.color.light04);
        colorCode.add(R.color.light05);
        colorCode.add(R.color.light06);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);
    }


    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_password_item, parent, false);
        return new PasswordViewHolder(view);
    }

    class PasswordViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, timestampTextView;
        RelativeLayout recyclerPassView;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.pass_title_text_view);
            timestampTextView = itemView.findViewById(R.id.pass_timestamp_text_view);
            //now password view holder is ready
            recyclerPassView = itemView.findViewById(R.id.recycler_pass);
        }
    }
}
