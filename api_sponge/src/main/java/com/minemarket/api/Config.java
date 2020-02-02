package com.minemarket.api;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Config {

    File file = new File("config/minemarketsponge/config.json");
    private JSONObject jsonObject;

    public Config() throws IOException {
        if (!file.exists()){
            new File("config/minemarketsponge/").mkdirs();
            file.createNewFile();
            Files.write(Paths.get(file.getPath()), "{\"key\":\"\"}".getBytes());
        }

        String text = readFile(file.getPath());
        jsonObject = new JSONObject(text);
    }

    public String getString(String path) {
        return jsonObject.getString(path);
    }

    public void setString(String path, String value) {
        jsonObject.put(path, value);
    }

    public void save() throws IOException {
        if (!file.exists()){
            new File("config/minemarketsponge/").mkdirs();
            file.createNewFile();
        }
        Files.write(Paths.get(file.getPath()), jsonObject.toString().getBytes());
    }

    private String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

}
