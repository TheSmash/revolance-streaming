package com.smash.revolance.camcast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ebour on 11/10/14.
 */
public class Playlist
{
    private       int  mediaIdx = 1;
    private List<Media> medias = new ArrayList<>();

    public void setMediaIdx(final int mediaIdx)
    {
        this.mediaIdx = mediaIdx;
    }

    public void addMedia(final Media media)
    {
        this.medias.add(media);
    }

    public void remove(int idx)
    {
        final Media media = this.medias.remove(idx);
        media.deleteFile();
    }

    public void increaseMediaIndex()
    {
        this.mediaIdx++;
    }

    public String toString()
    {
        final StringBuilder str = new StringBuilder();

        str.append("#EXTM3U\n");
        str.append("#EXT-X-VERSION:4\n");
        str.append("#EXT-X-TARGETDURATION:").append(getDuration()).append("\n");
        str.append("#EXT-X-MEDIA-SEQUENCE:").append(mediaIdx).append("\n");

        for(Media media : medias)
        {
            str.append("#EXTINF:"+media.duration+",\n");
            str.append(media.publicUrl).append("\n");
        }

        return str.toString();

    }

    public int getDuration()
    {
        int duration = 0;

        for(Media media : medias)
        {
            duration += media.duration;
        }

        return duration;
    }

    public int size()
    {
        return medias.size();
    }
}
