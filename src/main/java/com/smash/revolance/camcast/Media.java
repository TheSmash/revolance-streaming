package com.smash.revolance.camcast;

import java.io.File;

/**
 * Created by ebour on 11/10/14.
 */
public class Media
{
    public int duration;

    public String publicUrl;
    public String privateUrl;

    public void deleteFile()
    {
        new File(privateUrl).delete();
    }

}
