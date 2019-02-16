package com.teame.boostcamp.myapplication.model.entitiy;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GoodsListHeader implements Parcelable {

    private String key;
    private String uid;
    private String title;
    private Date startDate;
    private Date endDate;
    private String nation;
    private String city;
    private Double lat;
    private Double lng;

    public GoodsListHeader(){
    }

    public GoodsListHeader(String nation, String city, Date startDate, Date endDate, Double lat, Double lng){
        this.nation=nation;
        this.city=city;
        this.startDate=startDate;
        this.endDate=endDate;
        this.lat=lat;
        this.lng=lng;
    }

    protected GoodsListHeader(Parcel in) {
        key = in.readString();
        uid = in.readString();
        title = in.readString();
        long tmpDate = in.readLong();
        this.startDate = tmpDate == -1 ? null : new Date(tmpDate);
        tmpDate = in.readLong();
        this.endDate = tmpDate == -1 ? null : new Date(tmpDate);
        nation = in.readString();
        city = in.readString();
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readDouble();
        }
    }

    public static final Creator<GoodsListHeader> CREATOR = new Creator<GoodsListHeader>() {
        @Override
        public GoodsListHeader createFromParcel(Parcel in) {
            return new GoodsListHeader(in);
        }

        @Override
        public GoodsListHeader[] newArray(int size) {
            return new GoodsListHeader[size];
        }
    };

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        if(TextUtils.isEmpty(title)){
            return getDefaultTitle();
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public String getStringDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(startDate);
    }

    @Exclude
    public String getStringEndDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(endDate);
    }

    @Exclude
    public String getDefaultTitle(){
        return this.nation+"-"+this.city+this.getStringDate();
    }
    @Override
    public String toString() {
        return "GoodsListHeader{" +
                "title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", key='" + key + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(uid);
        parcel.writeString(title);
        parcel.writeLong(startDate != null ? startDate.getTime() : -1);
        parcel.writeLong(endDate != null ? endDate.getTime() : -1);
        parcel.writeString(nation);
        parcel.writeString(city);
        if (lat == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(lat);
        }
        if (lng == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(lng);
        }
    }
}
