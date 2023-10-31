package com.github.shawramland;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Entry {
    final private LocalDateTime timestamp;
    private String title;
    private String content;

    private int id;

    public Entry(int id, String title, String content, String timestamp) {
        this.id = id;
        this.timestamp = LocalDateTime.parse(timestamp);
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getTimeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setContent(String content){
        this.content = content;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter) + " - " + title + "\n" + content;
    }
}
