package com.tianxing.hotflyer.viewer.fragment.favourite;

import android.support.v7.widget.RecyclerView;

import com.tianxing.hotflyer.viewer.JAViewer;
import com.tianxing.hotflyer.viewer.adapter.ActressAdapter;
import com.tianxing.hotflyer.viewer.adapter.ItemAdapter;
import com.tianxing.hotflyer.viewer.view.decoration.ActressItemDecoration;

/**
 * Project: JAViewer
 */

public class FavouriteActressFragment extends FavouriteFragment {
    @Override
    public ItemAdapter adapter() {
        return new ActressAdapter(JAViewer.CONFIGURATIONS.getStarredActresses(), this.getActivity());
    }

    @Override
    public RecyclerView.ItemDecoration decoration() {
        return new ActressItemDecoration();
    }
}
