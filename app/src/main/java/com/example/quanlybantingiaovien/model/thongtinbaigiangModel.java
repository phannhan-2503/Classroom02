package com.example.quanlybantingiaovien.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class thongtinbaigiangModel implements Parcelable {

    private String src;
    private String tenGiangVien;
    private Date ngayDangTin;
    private String noiDungTin;
    private List<taptinModel> taptinModel;


    public thongtinbaigiangModel(String src,String tenGiangVien, Date ngayDangTin, String noiDungTin, List<taptinModel> taptinModel) {
        this.src=src;
        this.tenGiangVien = tenGiangVien;
        this.ngayDangTin = ngayDangTin;
        this.noiDungTin = noiDungTin;
        this.taptinModel = taptinModel;
    }
    public thongtinbaigiangModel(){

    }

    protected thongtinbaigiangModel(Parcel in) {
        src = in.readString();
        tenGiangVien = in.readString();
        noiDungTin = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(src);
        dest.writeString(tenGiangVien);
        dest.writeString(noiDungTin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<thongtinbaigiangModel> CREATOR = new Creator<thongtinbaigiangModel>() {
        @Override
        public thongtinbaigiangModel createFromParcel(Parcel in) {
            return new thongtinbaigiangModel(in);
        }

        @Override
        public thongtinbaigiangModel[] newArray(int size) {
            return new thongtinbaigiangModel[size];
        }
    };

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTenGiangVien() {
        return tenGiangVien;
    }

    public void setTenGiangVien(String tenGiangVien) {
        this.tenGiangVien = tenGiangVien;
    }

    public Date getNgayDangTin() {
        return ngayDangTin;
    }

    public void setNgayDangTin(Date ngayDangTin) {
        this.ngayDangTin = ngayDangTin;
    }

    public List<com.example.quanlybantingiaovien.model.taptinModel> getTaptinModel() {
        return taptinModel;
    }

    public void setTaptinModel(List<com.example.quanlybantingiaovien.model.taptinModel> taptinModel) {
        this.taptinModel = taptinModel;
    }


    public String getNoiDungTin() {
        return noiDungTin;
    }

    public void setNoiDungTin(String noiDungTin) {
        this.noiDungTin = noiDungTin;
    }

}
