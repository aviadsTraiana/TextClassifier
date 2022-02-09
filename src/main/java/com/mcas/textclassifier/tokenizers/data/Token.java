package com.mcas.textclassifier.tokenizers.data;

import lombok.Value;

@Value
public class Token {
    TokenType type;
    String value;
}
