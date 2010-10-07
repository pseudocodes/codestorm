package edu.sjtu.cse.codestorm.bean;

public class Message {
    private String id;

    public Message(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}