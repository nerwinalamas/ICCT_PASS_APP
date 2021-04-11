package com.example.icctpassapp.models;

import com.example.icctpassapp.Classrooms;
import com.example.icctpassapp.User;

public class ScanStudents {
    private User user;
    private Classrooms classrooms;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Classrooms getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(Classrooms classrooms) {
        this.classrooms = classrooms;
    }
}
