package com.smash.revolance.camcast;

import java.awt.image.BufferedImage;

/**
 * Created by ebour on 11/10/14.
 */
public interface VideoFeed extends Feed
{
    int getVideoRate();
    BufferedImage nextImage();

    int getWidth();
    int getHeight();
}
