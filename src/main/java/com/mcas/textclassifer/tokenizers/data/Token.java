package com.mcas.textclassifer.tokenizers.data;

import lombok.Value;

@Value
public class Token {
    TokenType type;
    String value;
}
