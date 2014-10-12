package com.smash.revolance.camcast;

import java.util.Map;

/**
 * Created by ebour on 11/10/14.
 */
public interface Feed
{
    public static final int DEFAULT_VIDEO_RATE = 32;

    String getId();

    Map<String, Feed> getFeeds();
    Feed getFeed(String feedId);

    boolean isOpen();

}
