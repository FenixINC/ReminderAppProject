package com.example.taras.reminerapp.db.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Taras Koloshmatin on 20.07.2018
 */
public class Remind implements Parcelable {

    public int id;

    public String title;

    public String description;


    protected Remind(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<Remind> CREATOR = new Creator<Remind>() {
        @Override
        public Remind createFromParcel(Parcel in) {
            return new Remind(in);
        }

        @Override
        public Remind[] newArray(int size) {
            return new Remind[size];
        }
    };

    public Remind() {

    }

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
