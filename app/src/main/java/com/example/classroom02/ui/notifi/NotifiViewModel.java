package com.example.classroom02.ui.notifi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotifiViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NotifiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifi fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}