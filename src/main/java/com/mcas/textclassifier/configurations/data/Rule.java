package com.mcas.textclassifier.configurations.data;

import lombok.Value;

import java.util.List;
@Value
public class Rule {
   String domain;
   List<String> indicators;
}
