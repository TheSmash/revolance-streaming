package com.smash.revolance.camcast;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HLSStreamEncoder extends Thread implements StreamTransformer
{

    private final File feedDir;
    private static final Logger LOG = Logger.getLogger(HLSStreamEncoder.class.getName());
    private final int videoChunkDuration;

    private Playlist       playlist;
    private Feed           feed;
    private CountDownLatch latch;
    private String         rootUrl;

    public HLSStreamEncoder(final File feedDir, final int videoChunkDuration)
    {
        this.feedDir = feedDir;
        if(!this.feedDir.exists())
        {
            this.feedDir.mkdirs();
        }

        this.playlist = new Playlist();
        this.videoChunkDuration = videoChunkDuration;
    }

    public void setFeed(Feed feed)
    {
        this.feed = feed;
    }

    public void run()
    {
        encode(this.feed);
    }

    @Override
    public void encode(final Feed feed)
    {
        AudioFeed audioFeed;
        VideoFeed videoFeed;

        if(feed instanceof VideoFeed)
        {
            videoFeed = (VideoFeed) feed;
        }
        else
        {
            videoFeed = (VideoFeed) feed.getFeed("video");
        }

        if(feed instanceof AudioFeed)
        {
            audioFeed = (AudioFeed) feed;
        }
        else
        {
            audioFeed = (AudioFeed) feed.getFeed("audio");
        }


        encode(feed, this.feedDir, audioFeed, videoFeed);

    }

    public void encode(final Feed feed, final File feedDir, final AudioFeed audioFeed, final VideoFeed videoFeed)
    {
        IMediaWriter encoder = null;
        File video = null;

        double mark = 0;
        long time = 0;
        int idx = 1;
        while(feed.isOpen())
        {
            try
            {
                time = Math.round(System.currentTimeMillis()-mark);
                if(time > videoChunkDuration)
                {
                    closeEncoder(encoder);

                    video = createTempFile(idx, feedDir);
                    idx ++;

                    encoder = ToolFactory.makeWriter(video.getAbsolutePath());
                    initEncoder(encoder, audioFeed, videoFeed);

                    updatePlaylist(video);

                    mark = System.currentTimeMillis();

                    if(playlist.size()==3)
                    {
                        latch.countDown();
                    }
                }

                encode(encoder, Math.round(System.currentTimeMillis()-mark), videoFeed, audioFeed);
            }
            catch(Exception e)
            {
                LOG.log(Level.INFO, e.getMessage(), e);
            }

        }

        closeEncoder(encoder);
    }

    private void updatePlaylist(final File video)
    {
        /*
        if(playlist.size() > MEDIA_WINDOW_SIZE)
        {
            playlist.remove(0);
            playlist.increaseMediaIndex();
        }
        */

        final Media media = new Media();
        media.duration = videoChunkDuration;
        media.privateUrl = video.getAbsolutePath();
        media.publicUrl = this.rootUrl + "/playlist/medias/" + video.getName();

        playlist.addMedia(media);
    }

    private static File createTempFile(final int idx, final File feedDir) throws IOException
    {
        File video = new File(feedDir, idx+".ts");
        LOG.log(Level.INFO, "new streaming chunk file: "+video);
        return video;
    }

    public static void closeEncoder(final IMediaWriter encoder)
    {
        if(encoder != null && encoder.isOpen())
        {
            // tell the writer to close and write the trailer if  needed
            encoder.close();

        }
    }

    public static void initEncoder(final IMediaWriter encoder, final AudioFeed audioFeed, final VideoFeed videoFeed)
    {
        if(encoder == null)
        {
            return;
        }

        if(videoFeed != null)
        {
            // We tell it we're going to add one video feed, with id 0,
            // at position 0, and that it will have a fixed frame rate of FRAME_RATE.
            encoder.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, videoFeed.getWidth(), videoFeed.getHeight());

        }

        if(audioFeed != null)
        {
            // Add audio feed
            encoder.addAudioStream(1, 0, 1, audioFeed.getAudioRate());

        }

    }

    public static void encode(final IMediaWriter encoder, final long time, final VideoFeed videoFeed, final AudioFeed audioFeed)
    {
        if(encoder != null)
        {
            if(videoFeed != null)
            {
                BufferedImage image = ConverterFactory.convertToType(videoFeed.nextImage(), BufferedImage.TYPE_3BYTE_BGR);
                IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

                IVideoPicture frame = converter.toPicture(image, time);
                frame.setKeyFrame(time == 0);
                frame.setQuality(0);

                encoder.encodeVideo(0, frame);
            }

            if(audioFeed != null)
            {
                // encode the audio feed to stream 1
                encoder.encodeAudio(1, audioFeed.nextSample());

            }
        }

    }

    private static void secureSleep(final double duration)
    {
        try
        {
            Thread.sleep(Math.round(duration));
        }
        catch (InterruptedException e)
        {
            // ignore
        }
    }

    public Playlist getPlaylist()
    {
        return playlist;
    }

    public void setLatch(final CountDownLatch latch)
    {
        this.latch = latch;
    }

    public void setRootUrl(String rootUrl)
    {
        this.rootUrl = rootUrl;
    }
}