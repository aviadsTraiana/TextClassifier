package com.mcas.textclassifier.types;

import lombok.Value;

@Value
public class Token {
    TokenType type;
    String value;
}
