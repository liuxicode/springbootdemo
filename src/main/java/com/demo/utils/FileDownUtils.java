package com.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Auther: liuxi
 * @Date: 2018/9/20 18:23
 * @Description:
 */
public class FileDownUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileDownUtils.class);

    public static InputStream download(String fileUrl) {
        FileOutputStream out = null;
        InputStream in = null;

        try{
            URL url = new URL(fileUrl);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

            // true -- will setting parameters
            httpURLConnection.setDoOutput(true);
            // true--will allow read in from
            httpURLConnection.setDoInput(true);
            // will not use caches
            httpURLConnection.setUseCaches(false);
            // setting serialized
            httpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
            // default is GET
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charsert", "UTF-8");
            // 1 min
            httpURLConnection.setConnectTimeout(60000);
            // 1 min
            httpURLConnection.setReadTimeout(60000);

            /*httpURLConnection.addRequestProperty("userName", userName);
            httpURLConnection.addRequestProperty("passwd", passwd);
            httpURLConnection.addRequestProperty("fileName", remoteFileName);*/

            // connect to server (tcp)
            httpURLConnection.connect();

            in = httpURLConnection.getInputStream();// send request to

            return in;
        }catch(Exception e){
            logger.error("file down fail:"+e.getMessage());
        }

        return null;
    }
}
