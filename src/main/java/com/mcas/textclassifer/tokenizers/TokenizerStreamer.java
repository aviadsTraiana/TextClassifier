package com.mcas.textclassifer.tokenizers;

import com.mcas.textclassifer.tokenizers.data.Token;
import com.mcas.textclassifer.tokenizers.data.TokenType;
import lombok.val;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TokenizerStreamer {
    private static final int QUOTE_CHARACTER = '\'';
    private static final int DOUBLE_QUOTE_CHARACTER = '"';
    final StreamTokenizer streamTokenizer;
    final TokenizerConfiguration configuration;

    public TokenizerStreamer(Reader reader, TokenizerConfiguration configuration) {
        this.streamTokenizer = new StreamTokenizer(reader);
        this.configuration = configuration;
        configureTokenizer();
    }

    private void configureTokenizer() {
        streamTokenizer.lowerCaseMode(configuration.lowerCaseMode);
        Optional.ofNullable(configuration.commentChar).ifPresent(streamTokenizer::commentChar);
        Optional.ofNullable(configuration.quote).ifPresent(streamTokenizer::quoteChar);
        Optional.ofNullable(configuration.whiteSpaceRange).ifPresent(r -> streamTokenizer.whitespaceChars(r.getLow(), r.getHigh()));
        Optional.ofNullable(configuration.wordRange).ifPresent(r -> streamTokenizer.wordChars(r.getLow(), r.getHigh()));
        Optional.ofNullable(configuration.ordinaryChars).ifPresent(r -> streamTokenizer.ordinaryChars(r.getLow(), r.getHigh()));
    }


    public Stream<Token> stream() {
        Iterator<Token> iter = new Iterator<Token>() {

            int nextToken = -1;

            @Override
            public boolean hasNext() {
                if (nextToken != StreamTokenizer.TT_EOF) {
                    return true;
                } else {
                    try {
                        nextToken = streamTokenizer.nextToken();
                        return (nextToken != StreamTokenizer.TT_EOF);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }

            @Override
            public Token next() {
                if (hasNext()) {
                    Token token;
                    if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                        val number = streamTokenizer.nval;
                        //potential bug: in text there is x.0 the output will be x
                        val string = number == Math.floor(number) ? String.valueOf((int)number) : String.valueOf(number);
                        token = new Token(TokenType.NUMBER, string);
                    } else if (streamTokenizer.ttype == StreamTokenizer.TT_WORD
                            || streamTokenizer.ttype == QUOTE_CHARACTER
                            || streamTokenizer.ttype == DOUBLE_QUOTE_CHARACTER) {
                        token = new Token(TokenType.WORD, streamTokenizer.sval);
                    } else {
                        token = new Token(TokenType.ORDINARY, String.valueOf((char) nextToken));
                    }
                    nextToken = -1;
                    return token;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iter, Spliterator.ORDERED | Spliterator.NONNULL), false);
    }

    public static List<Object> streamTokenizerWithDefaultConfiguration(Reader reader) throws IOException {
        StreamTokenizer streamTokenizer = new StreamTokenizer(reader);
        List<Object> tokens = new ArrayList<>();

        int currentToken = streamTokenizer.nextToken();
        while (currentToken != StreamTokenizer.TT_EOF) {

            if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                tokens.add(streamTokenizer.nval);
            } else if (streamTokenizer.ttype == StreamTokenizer.TT_WORD
                    || streamTokenizer.ttype == QUOTE_CHARACTER
                    || streamTokenizer.ttype == DOUBLE_QUOTE_CHARACTER) {
                tokens.add(streamTokenizer.sval);
            } else {
                tokens.add((char) currentToken);
            }

            currentToken = streamTokenizer.nextToken();
        }

        return tokens;
    }
}
