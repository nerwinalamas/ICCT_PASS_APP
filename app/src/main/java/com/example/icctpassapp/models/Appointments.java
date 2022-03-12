package com.example.icctpassapp.models;

import java.io.Serializable;

public class Appointments implements Serializable {

    private String apRoomName;
    private String apSubName;
    private String apTime;
    private String appointmentId;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }



    public String getApRoomName() {
        return apRoomName;
    }

    public void setApRoomName(String apRoomName) {
        this.apRoomName = apRoomName;
    }

    public String getApSubName() {
        return apSubName;
    }

    public void setApSubName(String apSubName) {
        this.apSubName = apSubName;
    }

    public String getApTime() {
        return apTime;
    }

    public void setApTime(String apTime) {
        this.apTime = apTime;
    }

}
