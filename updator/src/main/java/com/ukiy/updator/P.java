package com.ukiy.updator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by UKIY on 2016/1/8.
 */
public class P {
    private static String updateFileName = "update.apk";

    public static String getUpdataInfo(String updateUrl) throws IOException {
        InputStream is = null;
        HttpURLConnection urlCon = null;
        String resultData = "";
        try {
            URL url = new URL(updateUrl);
            urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(30000);
            urlCon.setReadTimeout(30000);
            urlCon.setDoInput(true); //允许输入流，即允许下载  urlCon.setDoInput(true);
            urlCon.setUseCaches(false); //不使用缓冲
            urlCon.setRequestMethod("GET"); //使用get请求
            is = urlCon.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                resultData += inputLine + "\n";
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (urlCon != null) {
                urlCon.disconnect();
            }
        }
        return resultData;
    }

    public static String downloadUpdateFile(String updateFileUrl) {
        InputStream is = null;
        HttpURLConnection urlCon = null;
        String resultData = "";
        try {
            URL url = new URL(updateFileUrl);
            urlCon = (HttpURLConnection) url.openConnection();
//            urlCon.setConnectTimeout(30000);
            urlCon.setReadTimeout(30000);
            urlCon.setDoInput(true); //允许输入流，即允许下载  urlCon.setDoInput(true);
            urlCon.setUseCaches(false); //不使用缓冲
            urlCon.setRequestMethod("GET"); //使用get请求
            is = urlCon.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                resultData += inputLine + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlCon != null) {
                urlCon.disconnect();
            }
        }
        return resultData;
    }

    //支持断点续传
    public static void downoadFile(boolean brokenReusme, String u, File f, DownloadCallback downloadCallback) throws IOException {
        int BLOCK_SIZE = 8192;
        InputStream in = null;
        RandomAccessFile out = null;
        long totalLen = 0;
        long startPos = 0;
        long len = 0;
        if (!brokenReusme) {
            f.delete();
        }
        try {
            if (f.exists()) {
                len = f.length();
                while (startPos + BLOCK_SIZE < len) {
                    startPos += BLOCK_SIZE;
                }
            }
            URL url = new URL(u);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("range", "bytes=" + startPos + "-");
            con.connect();
            int code = con.getResponseCode();
            if (code == 200) {//不支持断点续传，从头下载
                startPos = 0;
                f.delete();
                System.out.println("not support resume broken download");
            } else if (code != 206) {
                System.out.println("error: " + code);
                throw new IOException();
            }
            //code为206支持断点续传
            totalLen = con.getContentLength();
            if (totalLen <= 0)
                throw new RuntimeException("cannot get the length of the remote file");
            totalLen += startPos;//文件总长度为准备获取的长度加上起始长度
            out = new RandomAccessFile(f, "rw");
            out.seek(startPos);
            in = new BufferedInputStream(con.getInputStream());
            byte[] buf = new byte[BLOCK_SIZE];
            int readLen = -1;
            long downloadedLen = startPos;
            int progress = (int) (downloadedLen * 100 / totalLen);
            while ((readLen = in.read(buf)) != -1) {
                out.write(buf, 0, readLen);
                downloadedLen += readLen;
                if ((int) (downloadedLen * 100 / totalLen) - progress > 2) {//每百分之2才进一次
                    progress = (int) (downloadedLen * 100 / totalLen);
                    if (downloadCallback.onProgress((int) (downloadedLen * 100 / totalLen))) {

                    }
                }
            }
            if (totalLen != f.length()) {//文件长度等于总长度
                f.delete();
                throw new IOException("file size exception!");
            }

        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

    }
}
