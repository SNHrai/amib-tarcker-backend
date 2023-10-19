package com.tracker_application.tracker.model;

public class Message {
    private String sender;
    private String content;
    private String timeStamp;

    public Message() {

    }

    public Message(String sender, String content, String timeStamp) {
        this.sender = sender;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Message [sender=" + sender +
                ", content=" + content +
                ", timeStamp=" + timeStamp +
                "]";
    }

}
