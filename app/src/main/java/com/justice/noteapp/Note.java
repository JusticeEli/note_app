package com.justice.noteapp;

public class Note {
    private String noteData;
    private String id;
    private int category;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Note() {
    }

    public Note(String note, String id) {
        this.noteData = note;
        this.id = id;
    }

    public String getNoteData() {
        return noteData;
    }

    public void setNoteData(String noteData) {
        this.noteData = noteData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
