package com.smash.revolance.camcast;

import com.xuggle.ferry.IBuffer;
import com.xuggle.xuggler.IAudioSamples;

import javax.sound.sampled.*;

/**
 * Created by ebour on 11/10/14.
 */
public class MicrophoneFeed extends Stream implements AudioFeed
{
    private final byte[]         audioBuf;
    private final AudioFormat    audioFormat;

    private TargetDataLine       line = null;
    private AudioFileFormat.Type targetType = null;
    private AudioInputStream     audioInputStream = null;

    public MicrophoneFeed(final String feedId)
    {
        super(feedId);

        // audio buffer
        int numBytesToRead = 192000;
        audioBuf = new byte[numBytesToRead];

        audioFormat = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        try
        {
            this.line = (TargetDataLine) AudioSystem.getLine(info);
            this.line.open(audioFormat);

            this.targetType = AudioFileFormat.Type.WAVE;
            this.audioInputStream = new AudioInputStream(line);
        }
        catch (LineUnavailableException e)
        {
            System.out.println("unable to get a recording line");
            e.printStackTrace();
        }
    }

    @Override
    public int getAudioRate()
    {
        return (int) audioFormat.getSampleRate();
    }

    @Override
    public IAudioSamples nextSample()
    {
        if(line == null || targetType == null || audioInputStream == null)
        {
            return null;
        }
        else
        {
            return nextSample0();
        }
    }

    private IAudioSamples nextSample0()
    {
        long startTime = System.nanoTime();

        final int nBytesRead = line.read(audioBuf, 0, line.available());

        final IBuffer iBuf = IBuffer.make(null, audioBuf, 0, nBytesRead);

        final IAudioSamples smp = IAudioSamples.make(iBuf, 1,IAudioSamples.Format.FMT_S16);

        final long numSample = audioBuf.length/smp.getSampleSize();

        final long nanoTs = System.nanoTime() - startTime;
        smp.setComplete(true, numSample, (int) audioFormat.getSampleRate(), audioFormat.getChannels(), IAudioSamples.Format.FMT_S16, nanoTs/1000);

        return smp;
    }

    private AudioFormat getAudioFormat()
    {
        float sampleRate = 8000.0F;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }
}
