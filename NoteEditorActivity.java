package com.zybooks.fortnote;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteEditorActivity extends AppCompatActivity {

    private EditText etNoteTitle;
    private EditText etNoteContent;
    private NoteManager noteManager;
    private String noteId;
    private boolean isEditMode = false;
    private boolean isLocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        noteManager = new NoteManager(this);

        // Initialize views
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteContent = findViewById(R.id.etNoteContent);
        Button backButton = findViewById(R.id.back_button);
        Button lockButton = findViewById(R.id.lock_button);
        Button boldButton = findViewById(R.id.bold_button);
        Button italicButton = findViewById(R.id.italic_button);
        Button underlineButton = findViewById(R.id.under_button);
        FloatingActionButton fabSave = findViewById(R.id.fabSave);

        // Check if we're editing an existing note
        if (getIntent().hasExtra("note_id")) {
            isEditMode = true;
            noteId = getIntent().getStringExtra("note_id");
            String title = getIntent().getStringExtra("note_title");
            String content = getIntent().getStringExtra("note_content");

            etNoteTitle.setText(title);
            etNoteContent.setText(content);
        }

        // Back button
        backButton.setOnClickListener(v -> finish());

        // Lock button
        lockButton.setOnClickListener(v -> {
            isLocked = !isLocked;
            etNoteTitle.setEnabled(!isLocked);
            etNoteContent.setEnabled(!isLocked);

            if (isLocked) {
                lockButton.setBackgroundResource(android.R.drawable.ic_lock_lock);
                Toast.makeText(this, "Note locked", Toast.LENGTH_SHORT).show();
            } else {
                lockButton.setBackgroundResource(android.R.drawable.ic_lock_idle_lock);
                Toast.makeText(this, "Note unlocked", Toast.LENGTH_SHORT).show();
            }
        });

        // Initially set lock icon
        lockButton.setBackgroundResource(android.R.drawable.ic_lock_idle_lock);

        // Bold button
        boldButton.setOnClickListener(v -> applyStyle(Typeface.BOLD));

        // Italic button
        italicButton.setOnClickListener(v -> applyStyle(Typeface.ITALIC));

        // Underline button
        underlineButton.setOnClickListener(v -> applyUnderline());

        // Save button
        fabSave.setOnClickListener(v -> saveNote());
    }

    private void applyStyle(int style) {
        int start = etNoteContent.getSelectionStart();
        int end = etNoteContent.getSelectionEnd();

        if (start >= end) {
            Toast.makeText(this, "Please select text first", Toast.LENGTH_SHORT).show();
            return;
        }

        SpannableStringBuilder spannable = new SpannableStringBuilder(etNoteContent.getText());
        spannable.setSpan(new StyleSpan(style), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        etNoteContent.setText(spannable);
        etNoteContent.setSelection(start, end);
    }

    private void applyUnderline() {
        int start = etNoteContent.getSelectionStart();
        int end = etNoteContent.getSelectionEnd();

        if (start >= end) {
            Toast.makeText(this, "Please select text first", Toast.LENGTH_SHORT).show();
            return;
        }

        SpannableStringBuilder spannable = new SpannableStringBuilder(etNoteContent.getText());
        spannable.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        etNoteContent.setText(spannable);
        etNoteContent.setSelection(start, end);
    }

    private void saveNote() {
        if (isLocked) {
            Toast.makeText(this, "Note is locked. Unlock to save changes.", Toast.LENGTH_SHORT).show();
            return;
        }

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
}
