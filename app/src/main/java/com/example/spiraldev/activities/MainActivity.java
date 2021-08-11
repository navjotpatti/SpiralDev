package com.example.spiraldev.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.spiraldev.R;
import com.example.spiraldev.adapters.VideoViewAdapter;
import com.example.spiraldev.model.VideoModelClass;
import com.example.spiraldev.utils.Util;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public static final int PERMISSION_READ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Util.checkPermission(this)) {
            setRecyclerViewAndVideoList();
        }
    }

    private void setRecyclerViewAndVideoList() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        videoList = new ArrayList<>();
        getVideos();
    }

    public void getVideos() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                VideoModelClass videoModel = new VideoModelClass();
                videoModel.setTitle(title);
                videoModel.setUri(Uri.parse(data));
                videoModel.setDuration(Util.convertTime(Long.parseLong(duration)));
                Util.videoList.add(videoModel);

            } while (cursor.moveToNext());
        }

        VideoViewAdapter adapter = new VideoViewAdapter(this, Util.videoList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((pos, v) -> {
            Intent intent = new Intent(getApplicationContext(), VideoViewActivity.class);
            intent.putExtra("pos", pos);
            startActivity(intent);
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_READ) {
            if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                } else {
                    setRecyclerViewAndVideoList();
                }
            }
        }
    }
}