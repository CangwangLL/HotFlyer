package com.tianxing.hotlfyer;

import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tianxing.hotflyer.download.DelegateApplicationPackageManager;
import com.tianxing.hotflyer.viewer.JAViewer;

/**
 * Created by cangwang on 2018/5/22.
 */

public class HotFlyApplication extends JAViewer {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }

    @Override
    public String getPackageName() {
        if (Log.getStackTraceString(new Throwable()).contains("com.tianxing.hotflyer.download")) {
            return "com.xunlei.downloadprovider";
        }
        return super.getPackageName();
    }

    @Override
    public PackageManager getPackageManager() {
        if (Log.getStackTraceString(new Throwable()).contains("com.tianxing.hotflyer.download")) {
            return new DelegateApplicationPackageManager(super.getPackageManager());
        }
        return super.getPackageManager();
    }
}
