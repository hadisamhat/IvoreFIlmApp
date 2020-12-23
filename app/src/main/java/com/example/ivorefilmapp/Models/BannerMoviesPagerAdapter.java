package com.example.ivorefilmapp.Models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.example.ivorefilmapp.MovieDetails;
import com.example.ivorefilmapp.R;
import com.example.ivorefilmapp.SeriesDetail;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BannerMoviesPagerAdapter extends PagerAdapter {

    Context context;
    List<BannerMovies> bannerMoviesList;
    final String token;

    public BannerMoviesPagerAdapter(Context context, List<BannerMovies> bannerMoviesList, String token) {
        this.context = context;
        this.bannerMoviesList = bannerMoviesList;
        this.token = token;
    }

    @Override
    public int getCount() {
        return bannerMoviesList.size();
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.banner_layout,null);
        final ImageView bannerImage = view.findViewById(R.id.banner_image);

        Glide.with(context).load(bannerMoviesList.get(position).getImageUrl()).into(bannerImage);

        container.addView(view);

        bannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bannerMoviesList.get(position).getType() == "movie"){
                    Intent i  = new Intent(context, MovieDetails.class);
                    i.putExtra("movieName",bannerMoviesList.get(position).getMovieName());
                    i.putExtra("movieDescription",bannerMoviesList.get(position).getDescription());
                    i.putExtra("movieImage",bannerMoviesList.get(position).getImageUrl());
                    i.putExtra("movieId",bannerMoviesList.get(position).getId());
                    i.putExtra("accessToken",token);

                    context.startActivity(i);
                }
                if (bannerMoviesList.get(position).getType() == "serie"){
                    Intent i  = new Intent(context, SeriesDetail.class);
                    i.putExtra("seriesName",bannerMoviesList.get(position).getMovieName());
                    i.putExtra("seriesDescription",bannerMoviesList.get(position).getDescription());
                    i.putExtra("seriesImage",bannerMoviesList.get(position).getImageUrl());
                    i.putExtra("serieId",bannerMoviesList.get(position).getId());
                    i.putExtra("accessToken",token);
                    context.startActivity(i);
                }
            }
        });

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;


    }
}
