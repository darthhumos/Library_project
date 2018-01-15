package com.tmd.library;

/**
 * Created by Dovi Samuel on 14/01/2018.
 */

public class Message {

    private String from;
    private String to;
    private String subject;
    private String message;
    private String answer;
    private boolean read;
    private String Key;
    public Message()
    {
        this.from = "from";
        this.to = "temp1";
        this.subject = "tempi temp";
        this.message = "Temp message";
        this.answer = "the admin will get back to you soon";
        this.Key = "sdghfg";
        this.read = false;
    }

    public Message(String from, String to, String subject, String msg,String k) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = msg;
        this.answer = "the admin will get back to you soon";
        this.Key = k;
        this.read = false;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
