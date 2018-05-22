package com.tianxing.hotflyer.viewer.fragment.favourite;

import android.support.v7.widget.RecyclerView;

import com.tianxing.hotflyer.viewer.JAViewer;
import com.tianxing.hotflyer.viewer.adapter.ItemAdapter;
import com.tianxing.hotflyer.viewer.adapter.MovieAdapter;
import com.tianxing.hotflyer.viewer.view.decoration.MovieItemDecoration;

/**
 * Project: JAViewer
 */

public class FavouriteMovieFragment extends FavouriteFragment {
    @Override
    public ItemAdapter adapter() {
        return new MovieAdapter(JAViewer.CONFIGURATIONS.getStarredMovies(), this.getActivity());
    }

    @Override
    public RecyclerView.ItemDecoration decoration() {
        return new MovieItemDecoration();
    }
}
