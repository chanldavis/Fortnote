package com.zybooks.fortnote;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteEditorActivity extends AppCompatActivity {

    private EditText etNoteTitle;
    private EditText etNoteContent;
    private NoteManager noteManager;
    private String noteId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        noteManager = new NoteManager(this);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteContent = findViewById(R.id.etNoteContent);

        // Check if we're editing an existing note
        if (getIntent().hasExtra("note_id")) {
            isEditMode = true;
            noteId = getIntent().getStringExtra("note_id");
            String title = getIntent().getStringExtra("note_title");
            String content = getIntent().getStringExtra("note_content");

            etNoteTitle.setText(title);
            etNoteContent.setText(content);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Note");
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("New Note");
            }
        }

        // Set up save button
        FloatingActionButton fabSave = findViewById(R.id.fabSave);
        fabSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = etNoteTitle.getText().toString().trim();
        String content = etNoteContent.getText().toString().trim();

        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "Note is empty", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (isEditMode) {
            noteManager.updateNote(noteId, title, content);
            Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show();
        } else {
            noteManager.saveNote(title, content);
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}