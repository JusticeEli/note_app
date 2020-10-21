package com.justice.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

public class NoteActivity extends AppCompatActivity {

    private TextInputLayout noteTxtInput;
    private Note note = ApplicationClass.documentSnapshot.toObject(Note.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initWidgets();
        setDefaultValues();
    }

    private void setDefaultValues() {
        noteTxtInput.getEditText().setText(note.getNoteData());
    }

    private void initWidgets() {
        noteTxtInput = findViewById(R.id.noteTxtInput);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editMenu) {
            startActivity(new Intent(this, AddNoteActivity.class));
            finish();
        } else if (item.getItemId() == R.id.deleteMenu) {
            deleteNote();
            finish();
        }
        return super.onOptionsItemSelected(item);


    }

    private void deleteNote() {
        ApplicationClass.documentSnapshot.getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NoteActivity.this, "Deletetion success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(NoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
