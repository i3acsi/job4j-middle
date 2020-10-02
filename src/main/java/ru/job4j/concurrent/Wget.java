package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.concurrent.Callable;

public class Wget implements Callable<File> {
    private int loadLimit;
    private String url;

    Wget(String url, int speedLimit) {
        this.loadLimit = speedLimit * 1000;
        this.url = url;
    }

    @Override
    public File call() {
        String outFile = getOutFile(url);
        long downloaded = 0;
        try (BufferedInputStream in = new BufferedInputStream(new URL(this.url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                downloaded = downloaded + bytesRead;
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                if (downloaded >= loadLimit) {
                    Thread.sleep(1000);
                    downloaded = 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Wrong url to download");
            return null;
        }
        System.out.println("Loading is complete");
        return new File(outFile);
    }

    private String getOutFile(String url) {
        String[] tmp = url.split("/");
        return tmp[tmp.length - 1];
    }
}


