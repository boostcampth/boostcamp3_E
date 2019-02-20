package com.teame.boostcamp.myapplication.model.entitiy;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, Boolean> hashTag = new HashMap<>();

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> images) {
        Images = images;
    }

    private List<String> Images = new ArrayList<>();

    public GoodsListHeader() {
    }

    public GoodsListHeader(String nation, String city){
        this.nation = nation;
        this.city = city;
    }

    public GoodsListHeader(String nation, String city,Date start, Date end, Double lat, Double lng) {
        this.nation = nation;
        this.city = city;
        startDate=start;
        endDate=end;
        this.lat = lat;
        this.lng = lng;
    }

    public GoodsListHeader(String nation, String city, Double lat, Double lng) {
        this.nation = nation;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
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

        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            Boolean value = in.readInt() == 1;
            hashTag.put(key, value);
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


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        if (TextUtils.isEmpty(title)) {
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
    public String getDefaultTitle() {
        return this.nation + "-" + this.city + this.getStringDate();
    }


    public Map<String, Boolean> getHashTag() {
        return hashTag;
    }

    public void setHashTag(Map<String, Boolean> hashTag) {
        this.hashTag = hashTag;
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

        parcel.writeInt(hashTag.size());
        for (Map.Entry<String, Boolean> entry : hashTag.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeInt(entry.getValue() ? 1 : 0);
        }
    }
    @Override
    public String toString() {
        return "GoodsListHeader{" +
                "key='" + key + '\'' +
                ", uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", nation='" + nation + '\'' +
                ", city='" + city + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", hashTag=" + hashTag +
                ", Images=" + Images +
                '}';
    }

}
