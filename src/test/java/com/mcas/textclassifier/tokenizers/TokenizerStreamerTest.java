package com.mcas.textclassifier.tokenizers;

import com.mcas.textclassifier.types.TokenRange;
import com.mcas.textclassifier.types.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;

class TokenizerStreamerTest {

    TokenizerStreamer streamer;
    @BeforeEach
    void setUp() {
        streamer = new TokenizerStreamer(new StringReader("My medical insurance is\n" +
                " \"billed\" ,\n" +
                " from the Bank account executive number 84NK 4CcOun7 22"),
                TokenizerConfiguration
                        .builder()
                        .lowerCaseMode(true)
                        .wordRange(new TokenRange('a','z'))
                        .build()
        );
    }

    @Test
    void testStreamOfWords() {

        streamer.stream().filter(t->t.getType().equals(TokenType.WORD))
                .forEach(token ->{
           assertThat("every token must be lower cased", token.getValue().equals(token.getValue().toLowerCase()));
            for (char c: token.getValue().toCharArray()) {
                assertThat(
                        "every token which is a word must contain only alphanumeric characters, but contains "+token.getValue(),
                        Character.isLetterOrDigit((c))
                );
            }
        });
    }
    @Test
    void testStreamOfNumbers(){
        streamer.stream().filter(t->t.getType().equals(TokenType.NUMBER))
                .forEach(token ->{
                    for (char c: token.getValue().toCharArray()) {
                        assertThat(
                                "every token which is a word must contain only digits characters , but contains "+token.getValue(),
                                Character.isDigit(c)
                        );
                    }
                });
    }
}