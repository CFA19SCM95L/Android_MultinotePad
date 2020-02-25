package com.example.assignment2_multi_note_pad;


import java.util.Date;

public class Notes {

    private String title;
    private String content;
    private String date;

    public Notes(String title, String content) {
        this.title = title;
        this.content = content;
        date = new Date().toString();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
