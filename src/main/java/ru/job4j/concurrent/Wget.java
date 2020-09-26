package ru.job4j.concurrent;

import org.apache.commons.cli.*;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Для того, чтобы ограничить скорость скачивания, нужно проверять сколько байтов загрузиться за 1 секунду.
 * Если объем больше, то нужно выставлять паузу.
 * Пауза должна вычисляться, а не быть константой.
 * Пример кода для скачивания файла с задержкой в одну секунду.
 */

public class Wget {

    private String outFile;
    private long downloaded;
    private long totalLoaded;
    private Thread download;
    private boolean complete;
    private final static String LN = System.lineSeparator();

    Wget(String url, int speedLimit) {
        this.complete = false;
        String[] tmp = url.split("/");
        this.outFile = tmp[tmp.length - 1];
        download = new Thread(() -> {
            try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                long startTime = System.currentTimeMillis();
                long loadLimit = speedLimit * 1000;
                float speed;
                System.out.println("Out file name: " + outFile + LN);
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    this.downloaded = this.downloaded + bytesRead;
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                    if (this.downloaded >= loadLimit) {
                        Thread.sleep(1000);
                        speed = downloaded / (System.currentTimeMillis() - startTime);
                        startTime = System.currentTimeMillis();
                        this.totalLoaded += this.downloaded;
                        System.out.println("Speed: " + speed + "kB/s ## laded:" + this.totalLoaded + "bytes");
                        this.downloaded = 0;
                    }
                }
                this.complete = true;
                System.out.println("total loaded: " + this.totalLoaded + " bytes");

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    void begin() {
        this.download.start();
        while (!this.complete) {
            try {
                this.download.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        ArgsParser param = new ArgsParser(args);
        if (!param.isFailed()) {
            Wget wget = new Wget(param.getUrl(), param.getSpeedLimit());
            wget.download.start();
            wget.begin();
        }

    }
}

class ArgsParser {
    private String url;
    private Integer speedLimit;
    private boolean isFailed = false;
    private static final String MSG = "the command should be like this: java -jar wget.jar url 200"
            + System.lineSeparator() + "  where url is url of pic and 200 is speed limit";

    String getUrl() {
        return url;
    }

    Integer getSpeedLimit() {
        return speedLimit;
    }

    boolean isFailed() {
        return isFailed;
    }

    ArgsParser(String[] args) {
        Object[] result = parseArgs(args);
        if (!isFailed) {
            this.speedLimit = (int) result[1];
            this.url = (String) result[0];
        }
    }

    private Object[] parseArgs(String[] args) {
        Object[] result = new Object[2];
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);

            List<String> params = cmd.getArgList();
            if (params.size() < 2) {
                throw new ParseException("wrong args");
            }
            String tmp = params.get(0);
            if (checkUrl(tmp)) {
                result[0] = tmp;
            } else {
                throw new ParseException("wrong args");
            }
            tmp = params.get(1);
            String REGEX = "\\d+";
            boolean res = tmp.matches(REGEX);
            if (res) {
                result[1] = Integer.parseInt(tmp);
            } else {
                throw new ParseException("wrong args");
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(MSG, options);
            this.isFailed = true;
        }
        return result;
    }

    private boolean checkUrl(String input) {
        URL url;
        try {
            url = new URL(input);
        } catch (MalformedURLException e) {
            this.url = null;
            return false;
        }
        this.url = url.toString();
        return true;
    }
}
