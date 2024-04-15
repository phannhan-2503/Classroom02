package com.example.quanlybantingiaovien.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity(tableName = "nhanxet")
public class nhanxetModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int src;
    private String tenGiangVien;
    private Date ngayNhanXet;
    private String noiDungNhanXet;

    public nhanxetModel(int src, String tenGiangVien, Date ngayNhanXet, String noiDungNhanXet) {
        this.src = src;
        this.tenGiangVien = tenGiangVien;
        this.ngayNhanXet = ngayNhanXet;
        this.noiDungNhanXet = noiDungNhanXet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getNgayNhanXet() {
        return ngayNhanXet;
    }

    public void setNgayNhanXet(Date ngayNhanXet) {
        this.ngayNhanXet = ngayNhanXet;
    }

    public String getNoiDungNhanXet() {
        return noiDungNhanXet;
    }

    public void setNoiDungNhanXet(String noiDungNhanXet) {
        this.noiDungNhanXet = noiDungNhanXet;
    }
}
