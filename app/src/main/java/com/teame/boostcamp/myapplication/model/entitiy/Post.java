package com.teame.boostcamp.myapplication.model.entitiy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Post {
    private String content;
    private User userData;
    private String writer;
    private Object itemList;
    private int like;
    private String createdDate;
    private ArrayList<String> imagePathList;
    private ArrayList<String> likedUidList;

    public Post(){};

    public Post(String content, String writer, ArrayList<String> imagePathList){

        this.content = content;
        this.imagePathList = imagePathList;
        this.like = 0;
        this.writer = writer;
        this.likedUidList = new ArrayList<>();
        this.createdDate = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(System.currentTimeMillis());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }

    public Object getItemList() {
        return itemList;
    }

    public void setItemList(Object itemList) {
        this.itemList = itemList;
    }

    public ArrayList<String> getImagePathList() {
        return imagePathList;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setImagePathList(ArrayList<String> imagePathList) {
        this.imagePathList = imagePathList;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public ArrayList<String> getLikedUidList() {
        return likedUidList;
    }

    public void setLikedUidList(ArrayList<String> likedUidList) {
        this.likedUidList = likedUidList;
    }

    public String getLikeString(){
        return this.like+"";
    }

    public void increaseLike(){
        this.like += 1;
    }

    public void decreaseLike(){
        this.like -=1;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
