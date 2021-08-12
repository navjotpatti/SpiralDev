package com.example.spiraldev.activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.spiraldev.R;
import com.example.spiraldev.databinding.ActivityVideoViewBinding;
import com.example.spiraldev.utils.Util;
import com.example.spiraldev.viewmodel.MainViewModel;

public class VideoViewActivity extends AppCompatActivity {

    double currentPosition, duration;

    ActivityVideoViewBinding activityVideoViewBinding;
    MainViewModel rootViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVideoViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_view);
        rootViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        activityVideoViewBinding.setViewmodel(rootViewModel);

        rootViewModel.video_index = getIntent().getIntExtra("position", 0);
        rootViewModel.listModel.setVideoList(Util.tempList);

        registerCalls();
    }

    private void registerCalls() {
        rootViewModel.getNext().observe(this, result -> {
           nextVideo();
        });
        rootViewModel.getPrev().observe(this, result -> {
            prevVideo();
        });
        rootViewModel.getPlay().observe(this, this::playVideo);

        rootViewModel.getPause().observe(this, result -> {
            setPause();
        });
        rootViewModel.getVideoProgress().observe(this, result -> {
            setVideoProgress();
        });
    }

    // play video file
    public void playVideo(int pos) {
        try {
            activityVideoViewBinding.videoview.setVideoURI(rootViewModel.listModel.getVideoList().get(pos).getUri());
            activityVideoViewBinding.videoview.start();
            activityVideoViewBinding.pause.setImageResource(R.drawable.ic_baseline_pause_24);
            rootViewModel.video_index = pos;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVideoProgress() {
        //get the video duration
        currentPosition = activityVideoViewBinding.videoview.getCurrentPosition();
        duration = activityVideoViewBinding.videoview.getDuration();

        //display video duration
        activityVideoViewBinding.total.setText(Util.convertTime((long) duration));
        activityVideoViewBinding.current.setText(Util.convertTime((long) currentPosition));
        activityVideoViewBinding.seekbar.setMax((int) duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    currentPosition = activityVideoViewBinding.videoview.getCurrentPosition();
                    activityVideoViewBinding.current.setText(Util.convertTime((long) currentPosition));
                    activityVideoViewBinding.seekbar.setProgress((int) currentPosition);
                    handler.postDelayed(this, 500);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        activityVideoViewBinding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentPosition = seekBar.getProgress();
                activityVideoViewBinding.videoview.seekTo((int) currentPosition);
            }
        });
    }

    //play previous video
    public void prevVideo() {
        activityVideoViewBinding.prev.setOnClickListener(v -> {
            if (rootViewModel.video_index > 0) {
                rootViewModel.video_index--;
            } else {
                rootViewModel.video_index = rootViewModel.listModel.getVideoList().size() - 1;
            }
            playVideo(rootViewModel.video_index);
        });
    }

    //play next video
    public void nextVideo() {
        activityVideoViewBinding.next.setOnClickListener(v -> {
            if (rootViewModel.video_index < (rootViewModel.listModel.getVideoList().size() - 1)) {
                rootViewModel.video_index++;
            } else {
                rootViewModel.video_index = 0;
            }
            playVideo(rootViewModel.video_index);
        });
    }

    //pause video
    public void setPause() {
        activityVideoViewBinding.pause.setOnClickListener(v -> {
            if (activityVideoViewBinding.videoview.isPlaying()) {
                activityVideoViewBinding.videoview.pause();
                activityVideoViewBinding.pause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            } else {
                activityVideoViewBinding.videoview.start();
                activityVideoViewBinding.pause.setImageResource(R.drawable.ic_baseline_pause_24);
            }
        });
    }
}