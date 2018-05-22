package com.tianxing.hotflyer.viewer.fragment.genre;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.tianxing.hotflyer.viewer.JAViewer;
import com.tianxing.hotflyer.viewer.R;
import com.tianxing.hotflyer.viewer.adapter.GenreAdapter;
import com.tianxing.hotflyer.viewer.adapter.item.Genre;
import com.tianxing.hotflyer.viewer.view.ViewUtil;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class GenreFragment extends Fragment {

    protected List<Genre> genres = new ArrayList<>();

    public RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;

    public GenreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_list, container, false);
        mRecyclerView = view.findViewById(R.id.genre_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRecyclerView.setPadding(
                ViewUtil.dpToPx(1),
                ViewUtil.dpToPx(1),
                ViewUtil.dpToPx(1),
                ViewUtil.dpToPx(1)
        );

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(this.mAdapter = new GenreAdapter(genres, this.getActivity()));

        RecyclerView.ItemAnimator animator = new SlideInUpAnimator();
        animator.setAddDuration(300);
        mRecyclerView.setItemAnimator(animator);

        this.mAdapter.notifyItemRangeInserted(0, this.mAdapter.getItemCount());
    }

    public Call<ResponseBody> getCall(int page) {
        return JAViewer.SERVICE.getActresses(page);
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }
}
