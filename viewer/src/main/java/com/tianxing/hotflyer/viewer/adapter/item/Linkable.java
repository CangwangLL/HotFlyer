package com.tianxing.hotflyer.viewer.adapter.item;

import java.io.Serializable;

import static com.tianxing.hotflyer.viewer.JAViewer.Objects_equals;

/**
 * Project: JAViewer
 */
public class Linkable implements Serializable {

    public String link;

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object linkable) {
        if (!(linkable instanceof Linkable)) {
            return false;
        }

        return Objects_equals(link, ((Linkable) linkable).link);
    }

    @Override
    public String toString() {
        return "Linkable{" +
                "link='" + link + '\'' +
                '}';
    }
}
