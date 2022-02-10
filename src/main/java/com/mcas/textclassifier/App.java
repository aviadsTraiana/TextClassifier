package com.mcas.textclassifier;

import com.mcas.textclassifier.classifier.Classifier;
import com.mcas.textclassifier.configurations.data.ClassificationRules;
import com.mcas.textclassifier.configurations.loaders.ClassificationRulesLoader;
import com.mcas.textclassifier.configurations.loaders.JsonClassificationRulesLoader;
import com.mcas.textclassifier.tokenizers.TokenizerConfiguration;
import com.mcas.textclassifier.tokenizers.TokenizerStreamer;
import com.mcas.textclassifier.types.Token;
import com.mcas.textclassifier.types.TokenRange;
import com.mcas.textclassifier.types.TokenType;
import com.mcas.textclassifier.view.CommandLineView;
import lombok.Cleanup;
import lombok.val;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import static com.mcas.textclassifier.utils.FileHelper.readFile;

public class App {
    private static final TokenizerConfiguration tokenConfig =
            TokenizerConfiguration
                    .builder()
                    .lowerCaseMode(true)
                    .wordRange(new TokenRange('a', 'z'))
                    .build();

    public static void main(String[] args) {
        try {
            val cmd = new CommandLineView(args);
            val config = loadConfiguration(Paths.get(cmd.getConfigPath()));
            Classifier classifier = new Classifier(config);
            //todo: walk on all files
            //classifyFile(classifier, getPath("input_example.txt"));
            classifyFile(classifier, Paths.get(cmd.getScanPath()));
        }catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void classifyFile(Classifier classifier, Path filePath) throws IOException {
        @Cleanup Reader reader = readFile(filePath);
        val tokensStream =
                new TokenizerStreamer(reader, tokenConfig)
                        .stream()
                        .filter(tokenFilterPredicates());
        
        classifier
                .classifyTokens(tokensStream)
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
