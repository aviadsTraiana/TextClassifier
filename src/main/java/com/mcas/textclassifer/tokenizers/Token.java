package com.mcas.textclassifer.tokenizers;

import lombok.Value;

@Value
public class Token {
    TokenType type;
    String value;
}
