package com.example.classroom02.ui.exer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ExerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is exercise fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}