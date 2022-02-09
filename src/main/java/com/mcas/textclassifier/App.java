package com.mcas.textclassifier;

import com.mcas.textclassifier.classifier.Classifier;
import com.mcas.textclassifier.configurations.data.ClassificationRules;
import com.mcas.textclassifier.configurations.loaders.ClassificationRulesLoader;
import com.mcas.textclassifier.configurations.loaders.JsonClassificationRulesLoader;
import com.mcas.textclassifier.tokenizers.TokenizerConfiguration;
import com.mcas.textclassifier.tokenizers.TokenizerStreamer;
import com.mcas.textclassifier.tokenizers.data.TokenRange;
import com.mcas.textclassifier.view.CommandLineView;
import lombok.Cleanup;
import lombok.val;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.mcas.textclassifier.utils.PathHelper.getResoucePath;

public class App {
    private static final TokenizerConfiguration tokenConfig =
            TokenizerConfiguration
                    .builder()
                    .lowerCaseMode(true)
                    .wordRange(new TokenRange('a', 'z'))
                    .build();

    public static void main(String[] args) throws IOException, URISyntaxException {
        val cmd = new CommandLineView(args);
        val config = loadConfiguration(getResoucePath(cmd.getConfigPath()));
        Classifier classifier = new Classifier(config);
        //todo: walk on all files
        //classifyFile(classifier, getPath("input_example.txt"));
        classifyFile(classifier, getResoucePath(cmd.getScanPath()));
    }

    private static void classifyFile(Classifier classifier, Path filePath) throws IOException {
        @Cleanup Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
        val tokensStream = new TokenizerStreamer(reader, tokenConfig).stream();
        classifier.classifyTokens(tokensStream).forEach(System.out::println);
    }

    private static ClassificationRules loadConfiguration(Path path) throws IOException {
        @Cleanup Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        ClassificationRulesLoader cl = new JsonClassificationRulesLoader();
        return cl.load(reader);
    }
}
