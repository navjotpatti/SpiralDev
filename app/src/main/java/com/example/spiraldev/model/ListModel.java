package com.example.spiraldev.model;

import androidx.databinding.BaseObservable;

import com.example.spiraldev.BR;

import java.util.ArrayList;

public class ListModel extends BaseObservable {
    ArrayList<VideoModelClass> videoList  = new ArrayList<>();

    public ArrayList<VideoModelClass> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<VideoModelClass> videoList) {
        this.videoList = videoList;
        notifyPropertyChanged(BR._all);
    }
}
