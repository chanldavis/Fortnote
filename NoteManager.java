package com.zybooks.fortnote;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteManager {
    private static final String PREFS_NAME = "FortnotePrefs";
    private static final String NOTES_KEY = "notes";
    private SharedPreferences prefs;

    public NoteManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveNote(String title, String content) {
        List<Note> notes = getAllNotes();
        String id = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        Note newNote = new Note(id, title, content, timestamp);
        notes.add(0, newNote); // Add to beginning of list
        saveAllNotes(notes);
    }

    public void updateNote(String id, String title, String content) {
        List<Note> notes = getAllNotes();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(id)) {
                notes.get(i).setTitle(title);
                notes.get(i).setContent(content);
                notes.get(i).setTimestamp(System.currentTimeMillis());
                break;
            }
        }
        saveAllNotes(notes);
    }

    public void deleteNote(String id) {
        List<Note> notes = getAllNotes();
        notes.removeIf(note -> note.getId().equals(id));
        saveAllNotes(notes);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        String notesJson = prefs.getString(NOTES_KEY, "[]");

        try {
            JSONArray jsonArray = new JSONArray(notesJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Note note = new Note(
                        jsonObject.getString("id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("content"),
                        jsonObject.getLong("timestamp")
                );
                notes.add(note);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notes;
    }

    private void saveAllNotes(List<Note> notes) {
        JSONArray jsonArray = new JSONArray();

        for (Note note : notes) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", note.getId());
                jsonObject.put("title", note.getTitle());
                jsonObject.put("content", note.getContent());
                jsonObject.put("timestamp", note.getTimestamp());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        prefs.edit().putString(NOTES_KEY, jsonArray.toString()).apply();
    }
}