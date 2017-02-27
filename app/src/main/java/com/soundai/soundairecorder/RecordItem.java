package com.soundai.soundairecorder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fez on 2017/2/24.
 */

public class RecordItem implements Parcelable {

    private String name;
    private String filePath;
    private int id;
    private int length;
    private long time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public RecordItem() {

    }

    public RecordItem(Parcel in) {
        name = in.readString();
        filePath = in.readString();
        id = in.readInt();
        length = in.readInt();
        time = in.readLong();
    }

    public static final Creator<RecordItem> CREATOR = new Creator<RecordItem>() {
        @Override
        public RecordItem createFromParcel(Parcel in) {
            return new RecordItem(in);
        }

        @Override
        public RecordItem[] newArray(int size) {
            return new RecordItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(length);
        parcel.writeLong(time);
        parcel.writeString(filePath);
        parcel.writeString(name);
    }
}
