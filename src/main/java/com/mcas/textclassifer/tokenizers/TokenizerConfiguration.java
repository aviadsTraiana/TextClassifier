package com.mcas.textclassifer.tokenizers;

import lombok.Builder;

@Builder
public class TokenizerConfiguration {
    Character commentChar;
    Character quote;
    boolean lowerCaseMode;
    TokenRange wordRange;
    TokenRange whiteSpaceRange;
    TokenRange ordinaryChars;
}
