package com.tianxing.hotlfyer;

import android.support.multidex.MultiDex;

import com.tianxing.hotflyer.viewer.JAViewer;

/**
 * Created by Administrator on 2018/5/22.
 */

public class HotFlyApplication extends JAViewer {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
