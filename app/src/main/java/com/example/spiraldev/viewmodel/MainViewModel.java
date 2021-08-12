package com.example.spiraldev.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spiraldev.activities.VideoViewActivity;
import com.example.spiraldev.adapters.VideoViewAdapter;
import com.example.spiraldev.model.ListModel;
import com.example.spiraldev.model.VideoModelClass;
import com.example.spiraldev.utils.Util;

import java.io.File;
import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {
    public ListModel listModel = new ListModel();

    public int video_index = 0;

    public ListModel getListModel() {
        return listModel;
    }

    public void setListModel(ListModel listModel) {
        this.listModel = listModel;

    }

    public void setListLiveData(MutableLiveData<Integer> listLiveData) {
        this.listLiveData = listLiveData;
    }

    MutableLiveData<Integer> listLiveData = new MutableLiveData<>();
    MutableLiveData<Integer> play = new MutableLiveData<>();
    MutableLiveData<Integer> pause = new MutableLiveData<>();
    MutableLiveData<Integer> prev = new MutableLiveData<>();
    MutableLiveData<Integer> next = new MutableLiveData<>();
    MutableLiveData<Integer> videoProgress = new MutableLiveData<>();

    public MutableLiveData<Integer> getVideoProgress() {
        return videoProgress;
    }

    public MutableLiveData<Integer> getPlay() {
        return play;
    }

    public MutableLiveData<Integer> getPause() {
        return pause;
    }

    public MutableLiveData<Integer> getPrev() {
        return prev;
    }

    public MutableLiveData<Integer> getNext() {
        return next;
    }

    public MutableLiveData<Integer> getListLiveData() {
        return listLiveData;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @BindingAdapter(value = {"list", "viewmodel"})
    public static void bindList(RecyclerView recyclerView, ArrayList<VideoModelClass> dataList, MainViewModel mainViewModel) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
        VideoViewAdapter adapter = new VideoViewAdapter(recyclerView.getContext(), dataList);
        recyclerView.setAdapter(adapter);
        Util.tempList = dataList;
        adapter.setOnItemClickListener((pos, v) -> {
            mainViewModel.listLiveData.postValue(pos);

        });
    }

    @BindingAdapter(value = {"loadImage"})
    public static void loadImage(ImageView imageView, Uri uri) {
        Glide.with(imageView.getContext()).load(new File(uri.toString())).into(imageView);
    }


    @BindingAdapter(value = {"viewmodel"})
    public static void setVideoView(VideoView videoview, MainViewModel rootViewModel) {
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                rootViewModel.video_index++;
                if (rootViewModel.video_index >= (rootViewModel.listModel.getVideoList().size())) {
                    rootViewModel.video_index = 0;
                }
                rootViewModel.play.postValue(rootViewModel.video_index);

            }
        });

        videoview.setOnPreparedListener(mp -> rootViewModel.videoProgress.postValue(0));

        rootViewModel.play.postValue(rootViewModel.video_index);
        rootViewModel.prev.postValue(0);
        rootViewModel.next.postValue(0);
        rootViewModel.pause.postValue(0);
    }

    public void getVideosListFromStorage() {
        Cursor cursor = getApplication().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int counter = 1;
            do {

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                VideoModelClass videoModel = new VideoModelClass();
                videoModel.setTitle("Video " + counter);
                videoModel.setUri(Uri.parse(data));
                videoModel.setDuration(Util.convertTime(Long.parseLong(duration)));

                //add items separately in the list
                listModel.getVideoList().add(videoModel);
                counter++;

            } while (cursor.moveToNext());
        }
    }
}
