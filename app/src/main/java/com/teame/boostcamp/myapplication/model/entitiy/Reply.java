package com.teame.boostcamp.myapplication.model.entitiy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.teame.boostcamp.myapplication.util.DataStringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Reply extends BaseObservable implements Parcelable {

    private String key;
    private String content;
    private Double ratio;
    private Date writeDate;
    private String writer;
    private String errorMessage = null;

    public Reply() {

    }

    protected Reply(Parcel in) {
        key = in.readString();
        content = in.readString();
        if (in.readByte() == 0) {
            ratio = null;
        } else {
            ratio = in.readDouble();
        }
        long tmpDate = in.readLong();
        this.writeDate = tmpDate == -1 ? null : new Date(tmpDate);
        writer = in.readString();
        errorMessage = in.readString();
    }

    public static final Creator<Reply> CREATOR = new Creator<Reply>() {
        @Override
        public Reply createFromParcel(Parcel in) {
            return new Reply(in);
        }

        @Override
        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };

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
        if(ratio == null){
            return 0.0;
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(content);
        if (ratio == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(ratio);
        }
        parcel.writeLong(writeDate != null ? writeDate.getTime() : -1);
        parcel.writeString(writer);
        parcel.writeString(errorMessage);
    }
}
