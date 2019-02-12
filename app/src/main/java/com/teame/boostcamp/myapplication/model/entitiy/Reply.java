package com.teame.boostcamp.myapplication.model.entitiy;

import com.google.firebase.firestore.Exclude;
import com.teame.boostcamp.myapplication.util.DataStringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Reply  extends BaseObservable {

    private String key;
    private String content;
    private Double ratio;
    private Date writeDate;
    private String writer;
    private String errorMessage = null;

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Bindable
    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
        notifyPropertyChanged(BR.ratio);
    }
    @Bindable
    public Date getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Date writeData) {
        this.writeDate = writeData;
    }

    @Exclude
    public String getStringDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        dateFormat.format(writeDate);
        return DataStringUtil.CreateDataWithCheck(writeDate);
    }

    @Exclude
    @Bindable
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Bindable
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
