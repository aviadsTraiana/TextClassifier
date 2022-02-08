package com.mcas.textclassifer.configurations.loaders;

import com.mcas.textclassifer.configurations.data.ClassificationRules;

import java.io.Reader;

public interface ClassificationRulesLoader {
    ClassificationRules load(Reader reader);
}
