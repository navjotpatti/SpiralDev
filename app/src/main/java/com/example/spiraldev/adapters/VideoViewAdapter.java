package com.example.spiraldev.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spiraldev.R;
import com.example.spiraldev.databinding.ListItemsBinding;
import com.example.spiraldev.model.VideoModelClass;

import java.util.ArrayList;

public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.ViewHolder> {

    Context context;
    ArrayList<VideoModelClass> videoList;
    public OnItemClickListener onItemClickListener;

    public VideoViewAdapter (Context context, ArrayList<VideoModelClass> videoArrayList) {
        this.context = context;
        this.videoList = videoArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ListItemsBinding listItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_items, viewGroup, false);
        return new ViewHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        VideoModelClass videoModelClass = videoList.get(i);
        holder.listItemsBinding.setViewModelClass(videoModelClass);
    }

    @Override
    public int getItemCount() {
        return videoList!= null ? videoList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemsBinding listItemsBinding;

        public ViewHolder(ListItemsBinding listItemsBinding) {
            super(listItemsBinding.getRoot());

            this.listItemsBinding = listItemsBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
}
