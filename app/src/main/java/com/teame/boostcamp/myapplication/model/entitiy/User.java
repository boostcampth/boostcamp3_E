package com.teame.boostcamp.myapplication.model.entitiy;

public class User {

    private String userEmail;
    private String userPassword;
    private int userAge;
    private String userSex;

    public User() {
    }

    public User(String userEmail, String userPassword, int userAge, String userSex) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAge = userAge;
        this.userSex = userSex;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getUserAge() {
        return userAge;
    }

    public String getUserSex() {
        return userSex;
    }

    public String getUserPassword() {
        return userPassword;
    }

}
