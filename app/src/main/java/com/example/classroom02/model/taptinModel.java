package com.example.classroom02.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class taptinModel implements Parcelable {
    public String uri;

    public taptinModel(String uri) {
        this.uri = uri;
    }
    public taptinModel() {

    }

    protected taptinModel(Parcel in) {
        uri = in.readString();
    }

    public static final Creator<taptinModel> CREATOR = new Creator<taptinModel>() {
        @Override
        public taptinModel createFromParcel(Parcel in) {
            return new taptinModel(in);
        }

        @Override
        public taptinModel[] newArray(int size) {
            return new taptinModel[size];
        }
    };

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(uri);
    }
}
