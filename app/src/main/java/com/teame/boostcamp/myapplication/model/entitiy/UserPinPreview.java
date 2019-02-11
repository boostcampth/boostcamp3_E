package com.teame.boostcamp.myapplication.model.entitiy;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;

public class UserPinPreview {
    private Date start;
    private Date end;
    private String city;
    private String hashTag;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }


    @NonNull
    @Override
    public String toString() {
        String tostring="UserPinPreview: "
                +"StartDate: "+start.toString()+", "
                +"EndDate: "+end.toString()+", "
                +"Nation: "+city;
        return tostring;
    }
}
