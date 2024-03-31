package com.example.classroom02.ui.people;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PeopleViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PeopleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is people fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}