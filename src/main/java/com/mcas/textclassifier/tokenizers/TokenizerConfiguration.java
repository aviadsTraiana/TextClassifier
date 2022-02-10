package com.mcas.textclassifier.tokenizers;

import com.mcas.textclassifier.types.TokenRange;
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
