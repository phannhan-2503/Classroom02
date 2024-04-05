package com.example.quanlybantingiaovien.thongtinbaidang;

import java.util.Date;
import java.util.List;

public class thongtinbaigiangModel {
    private String tenGiangVien;
    private Date ngayDangTin;
    private String noiDungTin;
    private List<taptinModel> taptinModel;

    public thongtinbaigiangModel(String tenGiangVien, Date ngayDangTin, String noiDungTin, List<com.example.quanlybantingiaovien.thongtinbaidang.taptinModel> taptinModel) {
        this.tenGiangVien = tenGiangVien;
        this.ngayDangTin = ngayDangTin;
        this.noiDungTin = noiDungTin;
        this.taptinModel = taptinModel;
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

    public List<com.example.quanlybantingiaovien.thongtinbaidang.taptinModel> getTaptinModel() {
        return taptinModel;
    }

    public void setTaptinModel(List<com.example.quanlybantingiaovien.thongtinbaidang.taptinModel> taptinModel) {
        this.taptinModel = taptinModel;
    }


    public String getNoiDungTin() {
        return noiDungTin;
    }

    public void setNoiDungTin(String noiDungTin) {
        this.noiDungTin = noiDungTin;
    }

}
