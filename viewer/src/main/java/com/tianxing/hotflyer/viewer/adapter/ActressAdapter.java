package com.tianxing.hotflyer.viewer.adapter;

import android.app.Activity;
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
import com.tianxing.hotflyer.viewer.adapter.item.Actress;
import com.tianxing.hotflyer.viewer.view.SquareTopCrop;
import com.tianxing.hotflyer.viewer.view.listener.ActressClickListener;
import com.tianxing.hotflyer.viewer.view.listener.ActressLongClickListener;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

/**
 * Project: JAViewer
 */
public class ActressAdapter extends ItemAdapter<Actress, ActressAdapter.ViewHolder> {

    private Activity mParentActivity;

    public ActressAdapter(List<Actress> actresses, Activity mParentActivity) {
        super(actresses);
        this.mParentActivity = mParentActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_actress, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Actress actress = getItems().get(position);

        holder.parse(actress);

        holder.mLayout.setOnClickListener(new ActressClickListener(actress, mParentActivity));
        holder.mLayout.setOnLongClickListener(new ActressLongClickListener(actress, mParentActivity));

        holder.mImage.setImageDrawable(null);
        Glide.with(holder.mImage.getContext().getApplicationContext())
                .load(actress.getImageUrl())
                .placeholder(R.drawable.ic_movie_actresses)
                .diskCacheStrategy(SOURCE) // override default RESULT cache and apply transform always
                .skipMemoryCache(true) // do not reuse the transformed result while running
                .transform(new SquareTopCrop(holder.mImage.getContext()))
                .dontAnimate()
                .into(holder.mImage);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextName;

        public ImageView mImage;

        public View mLayout;

        public void parse(Actress actress) {
            mTextName.setText(actress.getName());
            mTextName.setSelected(true);
        }

        public ViewHolder(View view) {
            super(view);
            mTextName = view.findViewById(R.id.actress_name);
            mImage = view.findViewById(R.id.actress_img);
            mLayout = view.findViewById(R.id.layout_actress);
            ButterKnife.bind(this, view);
        }
    }
}
