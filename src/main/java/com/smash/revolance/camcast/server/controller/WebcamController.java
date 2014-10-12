package com.smash.revolance.camcast.server.controller;

import com.github.sarxos.webcam.Webcam;
import com.smash.revolance.camcast.HLSStreamEncoder;
import com.smash.revolance.camcast.Stream;
import com.smash.revolance.camcast.WebCamFeed;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * User: wsmash
 * Date: 08/06/13
 * Time: 18:44
 */
@Controller
public class WebcamController
{
    private static HLSStreamEncoder encoder;

    @RequestMapping(value = "/webcams/{webcamId}", method = RequestMethod.GET)
    public ModelAndView getWebcam(@PathVariable String webcamId, final HttpServletRequest request) throws IOException, InterruptedException
    {
        if(encoder == null)
        {
            final WebCamFeed webcamFeed = new WebCamFeed("video", Webcam.getWebcams().get(Integer.valueOf(webcamId)));
            webcamFeed.setDimension(new Dimension(640, 480));

            final Stream stream = new Stream(UUID.randomUUID().toString());
            stream.addFeed(webcamFeed);

            encoder = new HLSStreamEncoder(new File("/home/ebour/feeds"), 4000);
            encoder.setFeed(webcamFeed);
            encoder.setRootUrl(request.getRequestURL().toString());

            final CountDownLatch latch = new CountDownLatch(2);
            encoder.setLatch(latch);

            encoder.start();
            latch.countDown();

            Thread.sleep(20000);
        }

        final ModelMap model = new ModelMap();
        model.put("webcamId", webcamId);
        return new ModelAndView("Webcam", model);
    }

    @RequestMapping(value = "/webcams/{webcamId}/playlist", method = RequestMethod.GET)
    public void getWebcamPlaylist(@PathVariable String webcamId, final HttpServletResponse response) throws IOException
    {
        response.addHeader("Content-Type", "application/x-mpegURL");
        response.getWriter().write(encoder.getPlaylist().toString());
        response.setStatus(200);
    }

    @RequestMapping(value = "/webcams/{webcamId}/playlist/medias/{mediaId}", method = RequestMethod.GET)
    public void getWebcamMedia(@PathVariable String webcamId, @PathVariable String mediaId, final HttpServletResponse response) throws IOException
    {
        response.addHeader("Content-Type", "video/MP2T");

        final File file = new File("/home/ebour/feeds/"+mediaId+".ts");
        if(file.exists())
        {
            response.getOutputStream().write(FileUtils.readFileToByteArray(file));
            response.setStatus(200);
        }
        else
        {
            response.getOutputStream().write(new byte[]{});
        }
        response.setStatus(404);
    }

}
