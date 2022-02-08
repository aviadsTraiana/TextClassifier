package com.mcas.textclassifer;

import com.mcas.textclassifer.configurations.ClassificationRulesLoader;
import com.mcas.textclassifer.configurations.JsonClassificationRulesLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) throws IOException, URISyntaxException {
        loadConfiguration();
    }

    private static void loadConfiguration() throws URISyntaxException, IOException {
        URL configURL = App.class.getClassLoader().getResource("config.json");
        if (configURL == null) {
            throw new IllegalArgumentException("file not found!");
        }
        Path path = new File(configURL.toURI()).toPath();
        ClassificationRulesLoader cl = new JsonClassificationRulesLoader();
        System.out.println(cl.load(path));
    }
}
