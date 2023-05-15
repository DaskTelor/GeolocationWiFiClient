package com.nstu.geolocationwificlient.ui.fragment.locate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocateViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LocateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is location fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}