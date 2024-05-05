package com.example.quanlybantingiaovien.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class taptinModel implements Parcelable {
    public String tenLink;
    public String uri;

    public taptinModel(String uri) {
        this.uri = uri;
    }
    public taptinModel() {

    }

    public taptinModel(String tenLink, String uri) {
        this.tenLink = tenLink;
        this.uri = uri;
    }

    public String getTenLink() {
        return tenLink;
    }

    public void setTenLink(String tenLink) {
        this.tenLink = tenLink;
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
