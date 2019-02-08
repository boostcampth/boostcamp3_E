package com.teame.boostcamp.myapplication.model.entitiy;

import com.google.firebase.firestore.Exclude;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reply {

    private String content;
    private Double ratio;
    private Date writeDate;
    private String writer;
    private String errorMessage = null;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Date getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Date writeData) {
        this.writeDate = writeData;
    }

    @Exclude
    public String getStringDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(writeDate);
    }

    @Exclude
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }


    @Override
    public String toString() {
        return "Reply{" +
                ", content='" + content + '\'' +
                ", ratio=" + ratio +
                ", writeData=" + writeDate +
                '}';
    }

}
