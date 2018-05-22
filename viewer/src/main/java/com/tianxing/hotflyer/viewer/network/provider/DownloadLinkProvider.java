package com.tianxing.hotflyer.viewer.network.provider;

import java.util.List;

import com.tianxing.hotflyer.viewer.adapter.item.DownloadLink;
import com.tianxing.hotflyer.viewer.adapter.item.MagnetLink;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Project: JAViewer
 */
public abstract class DownloadLinkProvider {
    public abstract Call<ResponseBody> search(String keyword, int page);

    public abstract List<DownloadLink> parseDownloadLinks(String htmlContent);

    public abstract Call<ResponseBody> get(String url);

    public abstract MagnetLink parseMagnetLink(String htmlContent);

    public static DownloadLinkProvider getProvider(String name) {
        switch (name.toLowerCase().trim()) {
            case "btso":
                return new BTSOLinkProvider();
            case "torrentkitty":
                return new TorrentKittyLinkProvider();
            default:
                return null;
        }
    }
}
