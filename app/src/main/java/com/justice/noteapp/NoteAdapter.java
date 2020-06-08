package com.justice.noteapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.ViewHolder> {
    private Context context;

    public NoteAdapter(Context context, @NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Note model) {
        holder.textView.setText(model.getNoteData());

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            textView = itemView.findViewById(R.id.noteTxtView);

            setOnClickListeners();

        }

        private void setOnClickListeners() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplicationClass.documentSnapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    context.startActivity(new Intent(context, NoteActivity.class));
                }
            });

        }

    }
}
