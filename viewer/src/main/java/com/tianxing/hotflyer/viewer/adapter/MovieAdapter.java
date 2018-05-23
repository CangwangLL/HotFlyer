package com.tianxing.hotflyer.viewer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.tianxing.hotflyer.viewer.R;
import com.tianxing.hotflyer.viewer.activity.MovieActivity;
import com.tianxing.hotflyer.viewer.adapter.item.Movie;

/**
 * Project: JAViewer
 */
public class MovieAdapter extends ItemAdapter<Movie, MovieAdapter.ViewHolder> {

    private Activity mParentActivity;

    public MovieAdapter(List<Movie> movies, Activity mParentActivity) {
        super(movies);
        this.mParentActivity = mParentActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Movie movie = getItems().get(position);

        holder.parse(movie);

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mParentActivity, MovieActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie", movie);
                intent.putExtras(bundle);

                mParentActivity.startActivity(intent);
            }
        });

        holder.mImageCover.setImageDrawable(null);
        Glide.with(holder.mImageCover.getContext().getApplicationContext())
                .load(movie.getCoverUrl())
                .into(holder.mImageCover);

        holder.mImageHot.setVisibility(movie.isHot() ? View.VISIBLE : View.GONE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextTitle;

        public TextView mTextCode;

        public TextView mTextDate;

        public ImageView mImageCover;

        public ImageView mImageHot;

        public CardView mCard;

        public void parse(Movie movie) {
            mTextCode.setText(movie.getCode());
            mTextTitle.setText(movie.getTitle());
            mTextDate.setText(movie.getDate());
        }

        public ViewHolder(View view) {
            super(view);
            mTextTitle = view.findViewById(R.id.movie_title);
            mTextCode = view.findViewById(R.id.movie_size);
            mTextDate = view.findViewById(R.id.movie_date);
            mImageCover = view.findViewById(R.id.movie_cover);
            mImageHot = view.findViewById(R.id.movie_hot);
            mCard = view.findViewById(R.id.card_movie);
        }
    }
}