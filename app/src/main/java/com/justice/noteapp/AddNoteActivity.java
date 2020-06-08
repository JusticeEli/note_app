package com.justice.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNoteActivity extends AppCompatActivity {

    private TextInputLayout noteTxtInput;
    private Spinner categorySpinner;
    private FloatingActionButton cancelFob;
    private FloatingActionButton doneFob;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private boolean updating = false;
    private Note noteOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initWidgets();
        setOnClickListeners();
        setUpCategorySpinner();
        checkIfWeAreUpdatingOrAddingNote();
        setDefaultValues();

    }

    private void setDefaultValues() {
        if (updating) {
            noteTxtInput.getEditText().setText(noteOriginal.getNoteData());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    categorySpinner.setSelection(noteOriginal.getCategory());
                }
            }, 100);
        }
    }

    private void checkIfWeAreUpdatingOrAddingNote() {

        if (ApplicationClass.documentSnapshot != null) {
            updating = true;
            noteOriginal = ApplicationClass.documentSnapshot.toObject(Note.class);
        }
    }

    private void setUpCategorySpinner() {
        String[] categories = {"Medium", "High", "Low"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        categorySpinner.setAdapter(adapter);
    }

    private void setOnClickListeners() {
        cancelFob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        doneFob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void saveNote() {
        String noteData = noteTxtInput.getEditText().getText().toString();
        if (noteData.isEmpty()) {
            noteTxtInput.setError("Please fill");
            noteTxtInput.requestFocus();
            return;
        }

        Note note = new Note();
        note.setNoteData(noteData);
        if (updating) {
            updateNote(note);
        } else {
            setNoteCategoryAndSaveToFirebase(note);

        }


    }

    private void updateNote(Note note) {
        switch (categorySpinner.getSelectedItemPosition()) {
            case 0:
                note.setCategory(0);
                firebaseFirestore.collection("Notes").document(FirebaseAuth.getInstance().getUid()).collection("medium").document(noteOriginal.getId()).set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;
            case 1:
                note.setCategory(1);
                firebaseFirestore.collection("Notes").document(FirebaseAuth.getInstance().getUid()).collection("high").document(noteOriginal.getId()).set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;
            case 2:
                note.setCategory(2);
                firebaseFirestore.collection("Notes").document(FirebaseAuth.getInstance().getUid()).collection("low").document(noteOriginal.getId()).set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;

        }
    }

    private void setNoteCategoryAndSaveToFirebase(Note note) {
        switch (categorySpinner.getSelectedItemPosition()) {
            case 0:
                note.setCategory(0);
                firebaseFirestore.collection("Notes").document(FirebaseAuth.getInstance().getUid()).collection("medium").add(note).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 1:
                note.setCategory(1);
                firebaseFirestore.collection("Notes").document(FirebaseAuth.getInstance().getUid()).collection("high").add(note).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 2:
                note.setCategory(2);
                firebaseFirestore.collection("Notes").document(FirebaseAuth.getInstance().getUid()).collection("low").add(note).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

        }
    }

    private void initWidgets() {
        noteTxtInput = findViewById(R.id.noteTxtInput);
        categorySpinner = findViewById(R.id.categorySpinner);
        cancelFob = findViewById(R.id.cancelFob);
        doneFob = findViewById(R.id.doneFob);
    }
}
