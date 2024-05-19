package com.example.classroom02.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class fileModel implements Parcelable {
    public String uriFile;

    public fileModel(String uri) {
        this.uriFile = uriFile;
    }
    public fileModel() {

    }

    protected fileModel(Parcel in) {
        uriFile = in.readString();
    }

    public static final Creator<fileModel> CREATOR = new Creator<fileModel>() {
        @Override
        public fileModel createFromParcel(Parcel in) {
            return new fileModel(in);
        }

        @Override
        public fileModel[] newArray(int size) {return new fileModel[size];

        }
    };

    public String getUriFile() {
        return uriFile;
    }

    public void setUriFile(String uriFile) {
        this.uriFile = uriFile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(uriFile);
    }
}
