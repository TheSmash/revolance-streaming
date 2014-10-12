package com.smash.revolance.camcast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ebour on 11/10/14.
 */
public class Stream implements Feed
{
    private final String id;

    private final Map<String, Feed> feeds = new HashMap<>();

    public Stream(final String feedId)
    {
        this.id = feedId;
    }

    public void addFeed(Feed feed)
    {
        feeds.put(feed.getId(), feed);
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public Map<String, Feed> getFeeds()
    {
        return null;
    }

    @Override
    public Feed getFeed(String feedId)
    {
        return feeds.containsKey(feedId) ? feeds.get(feedId) : null;
    }

    @Override
    public boolean isOpen()
    {
        return true;
    }
}
