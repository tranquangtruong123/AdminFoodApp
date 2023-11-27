package com.example.admincnpm;

import java.io.Serializable;

public class UserAdmin {
    private String email;
    private String fullname;
    private String position;
    private String phoneNumber;
    private String password;
    private String pathImg;

    public UserAdmin() {
    }

    public UserAdmin(String email, String fullname, String position, String phoneNumber, String password, String pathImg) {
        this.email = email;
        this.fullname = fullname;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.pathImg = pathImg;
    }

    public UserAdmin(String email, String fullname, String position, String phoneNumber, String password) {
        this.email = email;
        this.fullname = fullname;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }
}
