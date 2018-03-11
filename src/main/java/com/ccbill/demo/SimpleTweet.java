package com.ccbill.demo;

/**
 * @author Kris Galea
 */
public class SimpleTweet {

    private String id;

    private String text;

    private String date;

    public SimpleTweet() {
    }

    public SimpleTweet(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SimpleTweet{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
