package com.example.xenologer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Recycler_Apod_Adapter extends RecyclerView.Adapter<Recycler_Apod_Adapter.Recycler_ViewHolder> {

    private ArrayList<ApodDetails> ItemList;
    private Context mContext;

    public static class Recycler_ViewHolder extends RecyclerView.ViewHolder{

        public TextView apodDate,apodDesc;
        public ImageView apodImage;
        public WebView apodVideo;

        @SuppressLint("SetJavaScriptEnabled")
        public Recycler_ViewHolder(@NonNull View itemView) {
            super(itemView);

            apodDate = itemView.findViewById(R.id.apodDate);
            apodDesc = itemView.findViewById(R.id.apodDesc);
            apodImage = itemView.findViewById(R.id.apodImage);
            apodVideo = itemView.findViewById(R.id.apodVideo);

            apodVideo.getSettings().setJavaScriptEnabled(true);
            apodVideo.setWebChromeClient(new WebChromeClient());

        }
    }

    public Recycler_Apod_Adapter(ArrayList<ApodDetails> list , Context context){
        ItemList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public Recycler_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.nasa_apod,parent,false);
        Recycler_ViewHolder recycler_viewHolder = new Recycler_ViewHolder(v);
        return recycler_viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_ViewHolder holder, int position) {
        ApodDetails details = ItemList.get(position);

        holder.apodDate.setText(details.getName());
        holder.apodDesc.setText(details.getDescription());

        if(!details.getImageUrl().equals("image")) {
            holder.apodVideo.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(details.getImageUrl())
                    .placeholder(R.drawable.ic_nasa_logo)
                    .circleCrop()
                    .into(holder.apodImage);
        }
        else if(!details.getVideoUrl().equals("noVideo")){
            holder.apodImage.setVisibility(View.GONE);
            holder.apodVideo.loadUrl(details.getVideoUrl());
        }
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }


}
