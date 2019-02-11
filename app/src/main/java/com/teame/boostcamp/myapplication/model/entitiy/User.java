package com.teame.boostcamp.myapplication.model.entitiy;

public class User {

    private String userEmail;
    private int userAge;
    private String userSex;

    public User() {
    }

    public User(String userEmail, int userAge, String userSex) {
        this.userEmail = userEmail;
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

}
