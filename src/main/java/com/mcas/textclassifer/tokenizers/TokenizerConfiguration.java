package com.mcas.textclassifer.tokenizers;

import com.mcas.textclassifer.tokenizers.data.TokenRange;
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
