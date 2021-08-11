package com.example.spiraldev.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.spiraldev.R;
import com.example.spiraldev.model.VideoModelClass;

import java.util.ArrayList;

public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.viewHolder> {

    Context context;
    ArrayList<VideoModelClass> videoList;
    public OnItemClickListener onItemClickListener;

    public VideoViewAdapter (Context context, ArrayList<VideoModelClass> videoArrayList) {
        this.context = context;
        this.videoList = videoArrayList;
    }

    @Override
    public VideoViewAdapter .viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_items, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoViewAdapter .viewHolder holder, final int i) {
        holder.title.setText(videoList.get(i).getTitle());
        holder.duration.setText(videoList.get(i).getDuration());
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, duration;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            duration = (TextView) itemView.findViewById(R.id.duration);

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
