package com.mcas.textclassifer.configurations.data;

import lombok.Value;

import java.util.List;

@Value
public class ClassificationRules {
    List<Rule> rules;
}
