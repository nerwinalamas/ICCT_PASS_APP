package com.example.icctpassapp.models;

import com.example.icctpassapp.User;

public class ScannedAppointment {
    public User user;
    public Appointments appointments;
    public HealthForm healthForm;

    public Appointments getAppointments() {
        return appointments;
    }

    public HealthForm getHealthForm() {
        return healthForm;
    }

    public void setHealthForm(HealthForm healthForm) {
        this.healthForm = healthForm;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public HealthForm getAppointments() {
//        return appointments;
//    }

    public void setAppointments(Appointments appointments) {
        this.appointments = appointments;
    }
}
