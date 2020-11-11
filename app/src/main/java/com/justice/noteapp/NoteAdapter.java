package com.justice.noteapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";
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
        private ImageView imageViewDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             textView = itemView.findViewById(R.id.noteTxtView);
             imageViewDelete = itemView.findViewById(R.id.imageViewDelete);

            setOnClickListeners();

        }

        private void setOnClickListeners() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplicationClass.documentSnapshot = getSnapshots().getSnapshot(getAdapterPosition());
               //     context.startActivity(new Intent(context, NoteFragment.class));
                }
            });

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSnapshots().getSnapshot(getAdapterPosition()).getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "onComplete: deletion success");
                            }else {
                                Log.d(TAG, "onComplete: deletion failed error: "+task.getException().getMessage());

                            }
                        }
                    });
                }
            });

        }

    }
}
