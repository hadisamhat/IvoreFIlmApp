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

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ItemViewHolder> {

    Context context;

    List<CategoryItem> categoryItemList;
    final String token;
    public SearchRecyclerAdapter(Context context, List<CategoryItem> categoryItemList, String token) {
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
