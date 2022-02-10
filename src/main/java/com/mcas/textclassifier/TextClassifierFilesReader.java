package com.mcas.textclassifier;

import com.mcas.textclassifier.classifier.Classifier;
import com.mcas.textclassifier.classifier.ds.Tag;
import com.mcas.textclassifier.configurations.data.ClassificationRules;
import com.mcas.textclassifier.configurations.loaders.ClassificationRulesLoader;
import com.mcas.textclassifier.configurations.loaders.JsonClassificationRulesLoader;
import com.mcas.textclassifier.tokenizers.TokenizerConfiguration;
import com.mcas.textclassifier.tokenizers.TokenizerStreamer;
import com.mcas.textclassifier.types.Token;
import com.mcas.textclassifier.types.TokenRange;
import com.mcas.textclassifier.types.TokenType;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

import static com.mcas.textclassifier.utils.FileHelper.readFile;

public class TextClassifierFilesReader implements Runnable {

    //final private Classifier classifier;
    final private Path scanPath;
    final private ClassificationRules classificationRules;

    private static final TokenizerConfiguration tokenConfig =
            TokenizerConfiguration
                    .builder()
                    .lowerCaseMode(true)
                    .wordRange(new TokenRange('a', 'z'))
                    .build();

    public TextClassifierFilesReader(Path config, Path scan) throws IOException {
        this.classificationRules = loadConfiguration(config);
        this.scanPath = scan;
    }

    @SneakyThrows
    @Override
    public void run() {
        @Cleanup val files = Files.walk(this.scanPath)
                .filter(Files::isRegularFile)
                .parallel();
        files.forEach(filePath -> {
            try {
                classifyFile(filePath);
            } catch (IOException e) {
                //todo: replace with logger
                System.err.println("could not read file " + filePath + " skipping");
            }
        });
    }

    private void classifyFile(Path filePath) throws IOException {
        @Cleanup Reader reader = readFile(filePath);
        val tokensStream =
                new TokenizerStreamer(reader, tokenConfig)
                        .stream()
                        .filter(tokenFilterPredicates());

        new Classifier(classificationRules)
                .classifyTokens(tokensStream)
                .stream()
                .map(Tag::getValue)
                .forEach(System.out::println);
    }

    private static ClassificationRules loadConfiguration(Path path) throws IOException {
        @Cleanup Reader reader = readFile(path);
        ClassificationRulesLoader cl = new JsonClassificationRulesLoader();
        return cl.load(reader);
    }

    private static Predicate<Token> tokenFilterPredicates() {
        return t -> t.getType().equals(TokenType.WORD) || t.getType().equals(TokenType.NUMBER);
    }
}
