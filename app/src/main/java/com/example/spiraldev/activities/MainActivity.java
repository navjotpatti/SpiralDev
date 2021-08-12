package com.example.spiraldev.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.spiraldev.databinding.ActivityMainBinding;
import com.example.spiraldev.model.VideoModelClass;
import com.example.spiraldev.utils.Util;
import com.example.spiraldev.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    MainViewModel rootViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        rootViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        activityMainBinding.setViewmodel(rootViewModel);

        //register live data calls to update the UI
        registerCalls();

        if (Util.isPermissionGranted(this)) {
            rootViewModel.getVideosListFromStorage();
        }
    }

    private void registerCalls() {
        rootViewModel.getListLiveData().observe(this, result -> {
            Intent intent = new Intent(getApplicationContext(), VideoViewActivity.class);
            intent.putExtra("position", result);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Util.PERMISSION_READ_GRANTED) {
            if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "Allow Storage Permission", Toast.LENGTH_LONG).show();
                } else {
                    rootViewModel.getVideosListFromStorage();
                }
            }
        }
    }
}