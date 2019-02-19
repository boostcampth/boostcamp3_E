package com.teame.boostcamp.myapplication.model.entitiy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.teame.boostcamp.myapplication.util.DataStringUtil;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Goods extends BaseObservable implements Parcelable, Cloneable {

    private String key;
    private String name;
    private Double ratio;
    private String lprice;
    private String img;
    private String link;
    private boolean isCheck = false;
    private String totalPrice;
    private int count = 1;
    private String userCustomUri;

    public Goods() {

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
        isCheck= in.readInt() == 1;
        count = in.readInt();
        userCustomUri = in.readString();
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

        try {
            info = minPriceResponse.getItems().get(0);
        } catch (Exception ignored) {
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
        if (ratio == null)
            return 0.0;
        return ratio;
    }


    public void setRatio(Double ratio) {
        this.ratio = ratio;
        notifyPropertyChanged(BR.ratio);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Bindable
    public String getLprice() {

        if (lprice == null) {
            return null;
        }
        return DataStringUtil.makeStringComma(lprice);
    }

    public void setLprice(String lprice) {
        this.lprice = lprice;
        notifyPropertyChanged(BR.lprice);
        notifyPropertyChanged(BR.totalPrice);
    }

    @Bindable
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
        notifyPropertyChanged(BR.totalPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Goods) {
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
        parcel.writeString(lprice);
        parcel.writeString(img);
        parcel.writeString(link);
        parcel.writeInt(isCheck ? 1 : 0);
        parcel.writeInt(count);
        parcel.writeString(userCustomUri);
    }

    @Bindable
    @Exclude
    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
        notifyPropertyChanged(BR.check);
    }

    @Bindable
    @Exclude
    public String getTotalPrice() {

        if (lprice == null) {
            return null;
        }

        int tmpLprice = Integer.valueOf(lprice);
        setTotalPrice(Integer.toString(tmpLprice * count));


        return DataStringUtil.makeStringComma(totalPrice);
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
        notifyPropertyChanged(BR.totalPrice);
    }

    @Bindable
    @Exclude
    public String getUserCustomUri() {
        return userCustomUri;
    }

    public void setUserCustomUri(String userCustomUri) {
        this.userCustomUri = userCustomUri;
        notifyPropertyChanged(BR.userCustomUri);
    }


    // 전체가격 계산을 위한 메소드
    @Exclude
    public int totalPrice() {
        if (lprice == null) {
            return 0;
        }
        int tmpLprice = Integer.valueOf(lprice);
        return tmpLprice * count;
    }

    @Override
    public Goods clone() {

        Goods clone;
        try {
            clone = (Goods) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); //should not happen
        }

        return clone;
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
                ", isCheck=" + isCheck +
                ", totalPrice='" + totalPrice + '\'' +
                ", count=" + count +
                ", userCustomUri=" + userCustomUri +
                '}';
    }

}
