package com.teame.boostcamp.myapplication.model.entitiy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.text.DecimalFormat;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Goods extends BaseObservable implements Parcelable {

    private String key;
    private String name;
    private Double ratio;
    private String lprice;
    private String img;
    private String link;
    private int count=0;

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
        lprice = in.readString();
        img = in.readString();
        link = in.readString();
        count = in.readInt();
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
        this.lprice = info.getLprice();
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public Double getRatio() {
        if(ratio == null)
            return 0.0;
        return ratio;
    }


    public void setRatio(Double ratio) {
        this.ratio = ratio;
        notifyPropertyChanged(BR.ratio);
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

    @Bindable
    @Exclude
    public String getLprice() {

        if(lprice == null){
            return null;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        try{
            Double lpriceDouble = Double.valueOf(lprice);
            return decimalFormat.format(lpriceDouble);
        }catch (Exception e){
            DLogUtil.e(e.getMessage());
            return null;
        }
    }

    public void setLprice(String lprice) {
        this.lprice = lprice;
        notifyPropertyChanged(BR.lprice);
    }

    @Bindable
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
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
    public String toString() {
        return "Goods{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", ratio=" + ratio +
                ", lprice='" + lprice + '\'' +
                ", img='" + img + '\'' +
                ", link='" + link + '\'' +
                ", count=" + count +
                '}';
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
        parcel.writeString(lprice);
        parcel.writeString(img);
        parcel.writeString(link);
        parcel.writeInt(count);
    }
}
