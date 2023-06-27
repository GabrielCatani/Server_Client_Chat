package edu.home42.sockets.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Message {
    private Long id;
    private User sender;
    private String message;
    private LocalDateTime timestamp;

    public Message() {
    }

    public Message(String msg) {
        this.message = msg;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return this.sender.getUsername() + ": " + this.message;
    }
}
