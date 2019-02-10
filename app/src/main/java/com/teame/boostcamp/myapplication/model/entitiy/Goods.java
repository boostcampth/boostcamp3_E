package com.teame.boostcamp.myapplication.model.entitiy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.teame.boostcamp.myapplication.util.DLogUtil;

public class Goods implements Parcelable {

    private String key;
    private String name;
    private Double ratio;
    private String lPrice;
    private String img;
    private String link;


    public Goods(){

    }
    protected Goods(Parcel in) {
        key = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            ratio = null;
        } else {
            ratio = in.readDouble();
        }
        lPrice = in.readString();
        img = in.readString();
        link = in.readString();
    }

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel in) {
            return new Goods(in);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };

    public void setMinPriceResponse(MinPriceResponse minPriceResponse) {
        MinPriceResponse.Item info;
        try{
            info = minPriceResponse.getItems().get(0);
        }catch (Exception ignored){
            return;
        }

        this.img = info.getImage();
        this.link = info.getLink();
        this.lPrice = info.getLprice();
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRatio() {
        if(ratio == null)
            return 0.0;
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    @Exclude
    public String getLPrice() {
        return lPrice;
    }

    public void setLPrice(String lPrice) {
        this.lPrice = lPrice;
    }

    @Exclude
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Exclude
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Item{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", ratio=" + ratio +
                ", lPrice='" + lPrice + '\'' +
                ", img='" + img + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Goods){
            Goods p = (Goods) o;
            return this.name.equals(p.getName());
        } else
            return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(name);
        if (ratio == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(ratio);
        }
        parcel.writeString(lPrice);
        parcel.writeString(img);
        parcel.writeString(link);
    }
}
