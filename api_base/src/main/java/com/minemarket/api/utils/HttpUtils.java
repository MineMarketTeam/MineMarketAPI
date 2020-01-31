package com.minemarket.api.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class HttpUtils {

    public static HttpURLConnection createConnection(String request, byte[] data) throws IOException {
        return sendPostData(createConnection(request), data);
    }

    public static HttpURLConnection createConnection(String request) throws IOException {
        return (HttpURLConnection) new URL(request).openConnection();
    }

    public static HttpURLConnection sendPostData(HttpURLConnection conn, byte[] data) throws IOException {
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(data.length));
        conn.setUseCaches(false);
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(data);
        wr.close();
        return conn;
    }

    public static String readSourceCode(URLConnection conn) throws IOException {
        InputStream is = conn.getInputStream();
        InputStreamReader re = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(re);

        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);
        in.close();

        return a.toString();
    }

    public static byte[] encodeUTFData(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }

}
