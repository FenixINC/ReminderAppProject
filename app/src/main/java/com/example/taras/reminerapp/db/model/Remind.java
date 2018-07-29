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

//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "local_id")
//    public Long localId;

    @PrimaryKey
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
    public String remindDate;

    @ColumnInfo(name = "type_remind")
    @SerializedName("typeRemind")
    public String typeRemind;

    public Remind() {
    }

    public Remind(String title, String remindDate, String description, String typeRemind) {
        this.title = title;
        this.remindDate = remindDate;
        this.description = description;
        this.typeRemind = typeRemind;
    }

    protected Remind(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        remindDate = in.readString();
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
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(remindDate);
        parcel.writeString(typeRemind);
    }
}