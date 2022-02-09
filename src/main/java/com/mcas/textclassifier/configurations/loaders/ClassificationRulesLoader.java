package com.mcas.textclassifier.configurations.loaders;

import com.mcas.textclassifier.configurations.data.ClassificationRules;

import java.io.Reader;

public interface ClassificationRulesLoader {
    ClassificationRules load(Reader reader);
}
