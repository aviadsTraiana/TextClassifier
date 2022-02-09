package com.mcas.textclassifer;

import com.mcas.textclassifer.configurations.loaders.ClassificationRulesLoader;
import com.mcas.textclassifer.configurations.loaders.JsonClassificationRulesLoader;
import com.mcas.textclassifer.tokenizers.*;
import lombok.Cleanup;
import lombok.val;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) throws IOException, URISyntaxException {
        loadConfiguration();
        streamTokens();
    }

    private static void streamTokens() throws IOException, URISyntaxException {
        @Cleanup BufferedReader br = Files.newBufferedReader(getPath("input_example.txt"), StandardCharsets.UTF_8);
        val tokenConfig =
                TokenizerConfiguration.builder()
                        .lowerCaseMode(true)
                        .wordRange(new TokenRange('a', 'z'))
                        .build();
        new TokenizerStreamer(br,tokenConfig).stream()
                .filter(t->t.getType().equals(TokenType.WORD))
                .map(Token::getValue)
                .forEach(System.out::println);
    }

    private static Path getPath(String filePath) throws URISyntaxException {
        URL configURL = App.class.getClassLoader().getResource(filePath);
        if (configURL == null) {
            throw new IllegalArgumentException("file not found!");
        }
        return new File(configURL.toURI()).toPath();
    }
    private static void loadConfiguration() throws URISyntaxException, IOException {
        Path path = getPath("config.json");
        @Cleanup Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        ClassificationRulesLoader cl = new JsonClassificationRulesLoader();
        val cr = cl.load(reader);
        cr.getRules().forEach(rule -> {
            //todo: add rule to a trie
        });
        System.out.println(cr);

    }
}
