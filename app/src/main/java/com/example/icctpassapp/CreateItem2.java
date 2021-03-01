package com.example.icctpassapp;

public class CreateItem2 {

    String eventName;
    String eventLocation;
    String time;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CreateItem2(String eventName, String eventLocation, String time) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.time = time;
    }
}
