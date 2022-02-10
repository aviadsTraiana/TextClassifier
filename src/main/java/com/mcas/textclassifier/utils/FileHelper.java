package com.mcas.textclassifier.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {
    public static BufferedReader readFile(Path filePath) throws IOException {
        return Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
    }
}
