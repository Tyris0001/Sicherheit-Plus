package com.sicherheitplus.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<List<String>> buttonTextList = new MutableLiveData<>();

    public GalleryViewModel() {
        // Initialize the button text list with some data (you can change this)
        List<String> initialData = new ArrayList<>();
        initialData.add("Button 1 Text");
        initialData.add("Button 2 Text");

        buttonTextList.setValue(initialData);
    }

    // Provide a LiveData object for the button text list
    public LiveData<List<String>> getButtonTextList() {
        return buttonTextList;
    }
}
