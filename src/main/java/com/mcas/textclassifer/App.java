package com.mcas.textclassifer;

import com.mcas.textclassifer.classifier.Classifier;
import com.mcas.textclassifer.configurations.data.ClassificationRules;
import com.mcas.textclassifer.configurations.loaders.ClassificationRulesLoader;
import com.mcas.textclassifer.configurations.loaders.JsonClassificationRulesLoader;
import com.mcas.textclassifer.tokenizers.data.TokenRange;
import com.mcas.textclassifer.tokenizers.TokenizerConfiguration;
import com.mcas.textclassifer.tokenizers.TokenizerStreamer;
import lombok.Cleanup;
import lombok.val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.mcas.textclassifer.utils.PathHelper.getPath;

public class App {
    private static final TokenizerConfiguration tokenConfig =
            TokenizerConfiguration
                    .builder()
                    .lowerCaseMode(true)
                    .wordRange(new TokenRange('a', 'z'))
                    .build();


    public static void main(String[] args) throws IOException, URISyntaxException {
        Classifier classifier = new Classifier(loadConfiguration());
        //todo: walk on all files
        //classifyFile(classifier, getPath("input_example.txt"));
        classifyFile(classifier, getPath("numbers.txt"));
    }

    private static void classifyFile(Classifier classifier, Path filePath) throws IOException {
        @Cleanup BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
        val tokensStream = new TokenizerStreamer(br, tokenConfig).stream();
        classifier.classifyTokens(tokensStream).forEach(System.out::println);
    }

    private static ClassificationRules loadConfiguration() throws URISyntaxException, IOException {
        Path path = getPath("config.json");
        @Cleanup Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        ClassificationRulesLoader cl = new JsonClassificationRulesLoader();
        return cl.load(reader);
    }
}
