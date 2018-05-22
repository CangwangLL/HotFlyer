package com.tianxing.hotflyer.viewer.fragment;

import com.tianxing.hotflyer.viewer.JAViewer;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Project: JAViewer
 */
public class HomeFragment extends MovieFragment {
    @Override
    public Call<ResponseBody> newCall(int page) {
        return JAViewer.SERVICE.getHomePage(page);
    }
}
