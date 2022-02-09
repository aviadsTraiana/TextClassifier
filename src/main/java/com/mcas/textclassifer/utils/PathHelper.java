package com.mcas.textclassifer.utils;

import com.mcas.textclassifer.App;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class PathHelper {
    public static Path getPath(String filePath) throws URISyntaxException {
        URL configURL = App.class.getClassLoader().getResource(filePath);
        if (configURL == null) {
            throw new IllegalArgumentException("file not found!");
        }
        return new File(configURL.toURI()).toPath();
    }
}
