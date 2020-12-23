package com.example.ivorefilmapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ivorefilmapp.Models.CategoryItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {

    Context context;
    List<CategoryItem> categoryItemList;
    final String token;
    public ItemRecyclerAdapter(Context context, List<CategoryItem> categoryItemList,String token) {
        this.context = context;
        this.categoryItemList = categoryItemList;
        this.token = token;

    }

    @NonNull
    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.cat_recycler_row_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemViewHolder holder, final int position) {
        Glide.with(context).load(categoryItemList.get(position).getImageUrl()).into(holder.itemImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryItemList.get(position).getType() == "movie"){
                    Intent i  = new Intent(context,MovieDetails.class);
                    i.putExtra("movieName",categoryItemList.get(position).getMovieName());
                    i.putExtra("movieDescription",categoryItemList.get(position).getDescription());
                    i.putExtra("movieImage",categoryItemList.get(position).getImageUrl());
                    i.putExtra("movieId",categoryItemList.get(position).getId());
                    i.putExtra("accessToken",token);
                    context.startActivity(i);
                }
                if (categoryItemList.get(position).getType() == "serie"){
                    Intent i  = new Intent(context,SeriesDetail.class);
                    i.putExtra("serieName",categoryItemList.get(position).getMovieName());
                    i.putExtra("serieDescription",categoryItemList.get(position).getDescription());
                    i.putExtra("serieImage",categoryItemList.get(position).getImageUrl());
                    i.putExtra("serieId",categoryItemList.get(position).getId());
                    i.putExtra("accessToken",token);
                    context.startActivity(i);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImage;
        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
        }
    }
}
