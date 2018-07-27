package com.example.taras.reminerapp.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Taras Koloshmatin on 20.07.2018
 */
@Entity(tableName = "tblRemind")
public class Remind implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    public Long localId;

    @ColumnInfo(name = "id")
    @SerializedName("id")
    public int id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    public String title;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    public String description;

    @ColumnInfo(name = "remind_date")
    @SerializedName("remindDate")
    public String date;

    @ColumnInfo(name = "type_remind")
    @SerializedName("typeRemind")
    public String typeRemind;

    public Remind() {
    }

    public Remind(String title, String description) {
        this.title = title;
        this.description = description;
    }

    protected Remind(Parcel in) {
        if (in.readByte() == 0) {
            localId = null;
        } else {
            localId = in.readLong();
        }
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        typeRemind = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (localId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(localId);
        }
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeString(typeRemind);
    }
}