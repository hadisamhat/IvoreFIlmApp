package com.example.ivorefilmapp.Models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ivorefilmapp.*;
import com.example.ivorefilmapp.databinding.ItemContainerEpisodeBinding;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ItemViewHolder> {

    Context context;
    List<Series> seriesList;
    final String token;
    public EpisodeAdapter(Context context, List<Series> seriesList,String token) {
        this.context = context;
        this.seriesList = seriesList;
        this.token = token;

    }

    @NonNull
    @NotNull
    @Override
    public EpisodeAdapter.ItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        ItemContainerEpisodeBinding itemContainerEpisodeBinding =  DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_container_episode,parent,false);
        return  new EpisodeAdapter.ItemViewHolder(itemContainerEpisodeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EpisodeAdapter.ItemViewHolder holder, final int position) {
        holder.bindEpisode(seriesList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(context,VideoActivity.class);
                sendIntent.putExtra("videoName", seriesList.get(position).episodeName);

                context.startActivity(sendIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder{

        private ItemContainerEpisodeBinding itemContainerEpisodeBinding;
        public ItemViewHolder(@NonNull ItemContainerEpisodeBinding itemContainerEpisodeBinding){
            super(itemContainerEpisodeBinding.getRoot());
            this.itemContainerEpisodeBinding = itemContainerEpisodeBinding;
        }

        public void bindEpisode(Series series){
            itemContainerEpisodeBinding.setEpisodeName(series.episodeName);
            itemContainerEpisodeBinding.setEpisodeLength(series.episodeLength);
        }
    }
}

