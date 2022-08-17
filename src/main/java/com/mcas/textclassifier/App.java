package com.mcas.textclassifier;

import com.mcas.textclassifier.view.CommandLineView;
import lombok.val;

import java.io.IOException;
import java.nio.file.Paths;

public class App {

    // java -jar build/libs/textclassifier.jar -cr /Users/aviads/git/temp/interviews/TextClassifier/src/main/resources/config.json -s /Users/aviads/git/temp/interviews/TextClassifier/src/main/resources/inputs
    public static void main(String[] args) {
        try {
            val cmd = new CommandLineView(args);
            val textClassifier = new TextClassifierFilesReader(
                    Paths.get(cmd.getConfigPath()),
                    Paths.get(cmd.getScanPath())
            );
            textClassifier.run();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
