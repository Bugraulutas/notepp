package com.bgr2.notepp;

import java.io.Serializable;

public class Notes implements Serializable {
    private String note_id;
    private String note_title;
    private String note;

    public Notes() {
    }

    public Notes(String note_id, String note_title, String note) {
        this.note_id = note_id;
        this.note_title = note_title;
        this.note = note;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
