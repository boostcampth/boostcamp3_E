package com.teame.boostcamp.myapplication.model.entitiy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.teame.boostcamp.myapplication.util.DataStringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Post implements Parcelable{
    private String uid;
    private String key;
    private String content;
    private String writer;
    private GoodsListHeader header;
    private int like;
    private Date createdDate;
    private Date modifiedDate;
    private Map<String,Boolean> tags;
    private ArrayList<String> imagePathList;
    private ArrayList<String> likedUidList;

    public Post(){};

    public Post(String uid, String content, String writer, GoodsListHeader header){
        this.uid = uid;
        this.content = content;
        this.like = 0;
        this.writer = writer;
        this.imagePathList = new ArrayList<>();
        this.likedUidList = new ArrayList<>();
        this.header = header;
    }

    protected Post(Parcel in) {
        uid = in.readString();
        key = in.readString();
        content = in.readString();
        writer = in.readString();
        header = in.readParcelable(GoodsListHeader.class.getClassLoader());
        like = in.readInt();
        imagePathList = in.createStringArrayList();
        likedUidList = in.createStringArrayList();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public Map<String, Boolean> getTags() {
        return tags;
    }

    public void setTags(Map<String, Boolean> tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public GoodsListHeader getHeader() {
        return header;
    }

    public void setHeader(GoodsListHeader header) {
        this.header = header;
    }

    public ArrayList<String> getImagePathList() {
        return imagePathList;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Exclude
    public String getStringDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        dateFormat.format(createdDate);
        return DataStringUtil.CreateDataWithCheck(createdDate);
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifyideDate) {
        this.modifiedDate = modifyideDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(key);
        dest.writeString(content);
        dest.writeString(writer);
        dest.writeParcelable(header, flags);
        dest.writeInt(like);
        dest.writeStringList(imagePathList);
        dest.writeStringList(likedUidList);
    }
}
