package com.mcas.textclassifier.view;

import org.apache.commons.cli.*;

public class CommandLineView {
    static final String CONFIG_OPTION_KEY = "config";
    static final String SCAN_OPTION_KEY = "scan";
    static final String HELP_OPTION_KEY = "help";
    Options options;
    CommandLine cmd;
    public CommandLineView(String[] args) throws IllegalArgumentException {
        this.options = new Options();
        options.addOption(new Option("cr",CONFIG_OPTION_KEY,true,"Path to a configuration file, representing the list of classification rules"));
        options.addOption(new Option("s", SCAN_OPTION_KEY,true,"Path in the filesystem representing a file or a folder that should be scanned"));
        options.addOption(new Option("h", HELP_OPTION_KEY,false,"helps how to work with the tool"));
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ant", options);
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
        checkForMissingOption(CONFIG_OPTION_KEY);
        checkForMissingOption(SCAN_OPTION_KEY);
    }

    public String getConfigPath(){
       return cmd.getOptionValue(CONFIG_OPTION_KEY);
    }
    public String getScanPath(){
        return cmd.getOptionValue(SCAN_OPTION_KEY);
    }

    private void checkForMissingOption(String optionKey) {
        if (!cmd.hasOption(optionKey)) {
            String errMessage = "Missing `--" + optionKey + "` option, type --help for more information";
            throw new IllegalArgumentException(errMessage);
        }
    }
}
