package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    //to use firebase data as a recycler view we need this class to be inherit from Fire store recycler adapter
    //that is firebase UI component we have to add the dependency for that
    //google - firebase UI dependencies

    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {

        //setting the data in text view for recycler view
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));
            //needed in data format so, converting it in utility class

        //now to edit the note in recycler firebase
        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, NoteDetailsActivity.class);
            //when we click on itemview it will go to NoteDetailsActivity
            //for that we have to pass uniqueId, title, content for that activity
            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);

            //obtaining the docId for each note
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);

            context.startActivity(intent);
        });
                //itemView is the object of class recycler item view
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creating a view from recycler note view item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);

        //returning view to
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView, timestampTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            contentTextView = itemView.findViewById(R.id.note_content_text_view);
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view);
            //now note view holder is ready
        }
    }
}
