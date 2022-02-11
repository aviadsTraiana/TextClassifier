package com.mcas.textclassifier.classifier;

import com.mcas.textclassifier.classifier.ds.Tag;
import com.mcas.textclassifier.configurations.data.ClassificationRules;
import com.mcas.textclassifier.configurations.data.Rule;
import com.mcas.textclassifier.types.Token;
import com.mcas.textclassifier.types.TokenType;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
class ClassifierTest {
    Classifier classifier;
    @BeforeEach
    void setUp() {
        val rules = new ArrayList<Rule>();
        rules.add(new Rule("financial", Arrays.asList("credit card","bank account")));
        rules.add(new Rule("healthcare", Collections.singletonList("Medical insurance")));
        rules.add(new Rule("hightech", Collections.singletonList("account executive")));
        classifier = new Classifier(new ClassificationRules(rules));
    }

    @Test
    void testClassifier(){
        val stream = Stream.of(
                "my",
                "medical",
                "insurance",
                "is",
                "billed",
                "from",
                "the",
                "bank",
                "account",
                "executive"
        ).map(word->new Token(TokenType.WORD,word));

        val tags = classifier.classifyTokens(stream).stream().map(Tag::getValue).collect(Collectors.toSet());
        assertThat(tags,containsInAnyOrder("financial","hightech","healthcare"));
    }
}