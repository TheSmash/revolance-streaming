package com.smash.revolance.camcast;

import com.xuggle.xuggler.IAudioSamples;

/**
 * Created by ebour on 11/10/14.
 */
public interface AudioFeed extends Feed
{
    int getAudioRate();
    IAudioSamples nextSample();
}
