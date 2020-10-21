package com.justice.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.justice.noteapp.MainActivity.COLLECTION_HIGH;
import static com.justice.noteapp.MainActivity.COLLECTION_LOW;
import static com.justice.noteapp.MainActivity.COLLECTION_MEDIUM;
import static com.justice.noteapp.MainActivity.COLLECTION_NOTES;

public class AddNoteActivity extends AppCompatActivity {

    private TextInputLayout noteTxtInput;
    private Spinner categorySpinner;
    private ExtendedFloatingActionButton cancelFob;
    private ExtendedFloatingActionButton doneFob;
    private ProgressDialog progressDialog;

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
        progressDialog.show();
        switch (categorySpinner.getSelectedItemPosition()) {
            case 0:
                note.setCategory(0);
                ApplicationClass.documentSnapshot.getReference().set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
                break;
            case 1:
                note.setCategory(1);
                ApplicationClass.documentSnapshot.getReference().set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
                break;
            case 2:
                note.setCategory(2);
                ApplicationClass.documentSnapshot.getReference().set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
                break;


          /*  case 2:
                note.setCategory(2);
                firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_LOW).document(noteOriginal.getId()).set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
                break;*/
        }
    }

    private void setNoteCategoryAndSaveToFirebase(Note note) {
        progressDialog.show();
        switch (categorySpinner.getSelectedItemPosition()) {
            case 0:
                note.setCategory(0);
                firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_MEDIUM).add(note).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
                break;
            case 1:
                note.setCategory(1);
                firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_HIGH).add(note).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
                break;
            case 2:
                note.setCategory(2);
                firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_LOW).add(note).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("saving data...");
    }
}

