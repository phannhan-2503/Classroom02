package com.example.quanlybantingiaovien.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

public class nhanxetModel extends thongtinbaigiangModel {

    public nhanxetModel(String src, String tenGiangVien, Date ngayNhanXet, String noiDungNhanxet) {
        super(src, tenGiangVien, ngayNhanXet, noiDungNhanxet, null);
    }

    public nhanxetModel() {
    }
}
