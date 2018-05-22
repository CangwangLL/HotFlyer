package com.tianxing.hotflyer.viewer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.tianxing.hotflyer.viewer.R;
import com.tianxing.hotflyer.viewer.activity.MovieListActivity;
import com.tianxing.hotflyer.viewer.adapter.item.Genre;

/**
 * Project: JAViewer
 */
public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private List<Genre> genres;

    private Activity mParentActivity;

    public GenreAdapter(List<Genre> genres, Activity mParentActivity) {
        this.genres = genres;
        this.mParentActivity = mParentActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_genre, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Genre genre = genres.get(position);
        holder.parse(genre);

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genre.getLink() != null) {
                    Intent intent = new Intent(mParentActivity, MovieListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", genre.getName());
                    bundle.putString("link", genre.getLink());

                    intent.putExtras(bundle);

                    mParentActivity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return genres == null ? 0 : genres.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextName;

        public CardView mCard;

        public void parse(Genre genre) {
            mTextName.setText(genre.getName());
        }

        public ViewHolder(View view) {
            super(view);
            mTextName = view.findViewById(R.id.genre_name);
            mCard = view.findViewById(R.id.card_genre);
        }
    }
}
