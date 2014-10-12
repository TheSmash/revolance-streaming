package com.smash.revolance.camcast;

import com.github.sarxos.webcam.Webcam;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.util.UUID;

/**
 * Created by ebour on 11/10/14.
 */
public class VideoFeedTest
{
    File            feeds  = new File("target/feeds");

    @Test
    public void shouldStoreVideoInSeveralFiles() throws Exception
    {
        WebCamFeed webcamFeed = new WebCamFeed("video", Webcam.getWebcams().get(0));
        webcamFeed.setDimension(new Dimension(640, 480));

        Stream stream = new Stream(UUID.randomUUID().toString());
        stream.addFeed(webcamFeed);

        HLSStreamEncoder encoder = new HLSStreamEncoder(feeds, 4000);
        encoder.encode(webcamFeed);

    }

}
