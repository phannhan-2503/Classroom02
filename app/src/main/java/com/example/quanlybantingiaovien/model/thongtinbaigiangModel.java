package com.example.quanlybantingiaovien.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class thongtinbaigiangModel implements Serializable {

    private int src;
    private String tenGiangVien;
    private Date ngayDangTin;
    private String noiDungTin;
    private List<taptinModel> taptinModel;

    public thongtinbaigiangModel(int src,String tenGiangVien, Date ngayDangTin, String noiDungTin, List<com.example.quanlybantingiaovien.model.taptinModel> taptinModel) {
        this.src=src;
        this.tenGiangVien = tenGiangVien;
        this.ngayDangTin = ngayDangTin;
        this.noiDungTin = noiDungTin;
        this.taptinModel = taptinModel;
    }
    public thongtinbaigiangModel(){

    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
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
