package com.example.classroom02.model;

import com.google.firebase.database.Exclude;

import java.util.List;

public class UploadModel{

    private String dataTitle;
    private String dataDesc;
    private String dataImage;
    private String dataStart;
    private String dataEnd;
    private  List<fileModel> fileModel;

    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataImage() {
        return dataImage;
    }

    public String getDataStart() {
        return dataStart;
    }

    public String getDataEnd() {
        return dataEnd;
    }

    public List<com.example.classroom02.model.fileModel> getFileModel() {
        return fileModel;
    }

    public void setFileModel(List<com.example.classroom02.model.fileModel> fileModel) {
        this.fileModel = fileModel;
    }



    public UploadModel(String dataTitle, String dataDesc, String dataImage, String dataStart, String dataEnd) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataImage = dataImage;
        this.dataStart = dataStart;
        this.dataEnd = dataEnd;
//        this.fileModel = fileModel;
    }


}
