package com.example.icctpassapp.models;

import com.example.icctpassapp.User;

public class ScannedEvent {
    private User user;
    private Events events;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }
}
