package com.smash.revolance.camcast;

import com.github.sarxos.webcam.Webcam;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ebour on 11/10/14.
 */
public class WebCamFeed extends Stream implements VideoFeed
{
    private final Webcam               webcam;


    public WebCamFeed(final String feedId, final Webcam webcam)
    {
        super(feedId);
        this.webcam = webcam;
    }

    public void setDimension(final Dimension dimension)
    {
        webcam.setViewSize(dimension);
    }

    @Override
    public BufferedImage nextImage()
    {
        open();
        return webcam.getImage();
    }

    @Override
    public int getWidth()
    {
        open();
        return (int) webcam.getViewSize().getWidth();
    }

    private void open()
    {
        if(!webcam.isOpen())
        {
            webcam.open(true);
        }
    }

    @Override
    public int getHeight()
    {
        open();
        return (int) webcam.getViewSize().getHeight();
    }

    @Override
    public int getVideoRate()
    {
        return Feed.DEFAULT_VIDEO_RATE;
    }

}
