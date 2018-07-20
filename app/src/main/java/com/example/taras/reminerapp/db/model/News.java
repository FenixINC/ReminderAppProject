package com.example.taras.reminerapp.db.model;

import android.os.Parcel;

/**
 * Created by Taras Koloshmatin on 20.07.2018
 */
public class News extends Remind {

    public int id;

    public String title;

    public String description;

    public News(String title, String description) {
        this.title = title;
        this.description = description;
    }

    protected News(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
    }
}
