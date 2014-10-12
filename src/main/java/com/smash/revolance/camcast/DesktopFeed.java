package com.smash.revolance.camcast;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ebour on 11/10/14.
 */
public class DesktopFeed extends Stream implements VideoFeed
{
    private static final Dimension screenBounds;

    static
    {
        screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
    }

    public DesktopFeed(String feedId)
    {
        super(feedId);
    }

    @Override
    public BufferedImage nextImage()
    {
        try
        {
            final Robot robot = new Robot();
            final Rectangle captureSize = new Rectangle(screenBounds);
            return robot.createScreenCapture(captureSize);
        }
        catch (AWTException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getWidth()
    {
        return (int) screenBounds.getWidth();
    }

    @Override
    public int getHeight()
    {
        return (int) screenBounds.getHeight();
    }

    @Override
    public int getVideoRate()
    {
        return Feed.DEFAULT_VIDEO_RATE;
    }

}
