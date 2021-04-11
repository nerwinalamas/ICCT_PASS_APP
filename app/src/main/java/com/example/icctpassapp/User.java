package com.example.icctpassapp;

public class User {

    public String Fullname, Course, Email, Password;

    public User() {
    }

    public User(String Fullname, String Email, String Password, String Course) {
        this.Fullname = Fullname;
        this.Course = Course;
        this.Email = Email;
        this.Password = Password;
    }
}
