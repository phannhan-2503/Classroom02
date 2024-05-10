package com.example.quanlybantingiaovien.model;


import com.google.firebase.database.Exclude;

public class nhanxetModel extends thongtinbaigiangModel {
    @Exclude
    private String key_user;
    @Exclude
    private String key_bangtin;

    public nhanxetModel(String key_bangtin,String src, String tenGiangVien, String ngayNhanXet, String noiDungNhanxet) {
        super(src, tenGiangVien, ngayNhanXet, noiDungNhanxet, null);
        this.key_bangtin= key_bangtin;
    }
    public nhanxetModel(String src, String tenGiangVien, String ngayNhanXet, String noiDungNhanxet) {
        super(src, tenGiangVien, ngayNhanXet, noiDungNhanxet, null);
    }

    public nhanxetModel() {
    }
    public void setKey_user(String key_user) {
        this.key_user = key_user;
    }
//    public nhanxetModel(String key_user) {
//        this.key_user=key_user;
//    }

    public String getKey_user() {
        return key_user;
    }

    public String getKey_bangtin() {
        return key_bangtin;
    }

    public void setKey_bangtin(String key_bangtin) {
        this.key_bangtin = key_bangtin;
    }
}
