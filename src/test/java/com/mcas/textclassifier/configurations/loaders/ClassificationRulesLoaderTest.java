package com.mcas.textclassifier.configurations.loaders;

import com.mcas.textclassifier.configurations.data.ClassificationRules;
import com.mcas.textclassifier.configurations.data.Rule;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ClassificationRulesLoaderTest {

    public static final String CLASSIFICATION_RULES_JSON_REPR = "{\n" +
            "  \"classification_rules\": [\n" +
            "    {\"domain\": \"financial\", \"indicators\": [\"credit card\", \"bank account\"]},\n" +
            "    {\"domain\": \"healthcare\", \"indicators\": [\"medical insurance\"]},\n" +
            "    {\"domain\": \"hightech\", \"indicators\": [\"account executive\"]},\n" +
            "  ]\n" +
            "}";
    ClassificationRulesLoader loader;

    @Test
    void jsonLoader() {
        loader = new JsonClassificationRulesLoader();
        val loadedClassificationRules =
                loader.load(new StringReader(CLASSIFICATION_RULES_JSON_REPR));

        val rules = new ArrayList<Rule>();
        rules.add(new Rule("financial", Arrays.asList("credit card", "bank account")));
        rules.add(new Rule("healthcare", Collections.singletonList("medical insurance")));
        rules.add(new Rule("hightech", Collections.singletonList("account executive")));
        val expectedClassificationRules = new ClassificationRules(rules);

        assertThat(expectedClassificationRules, equalTo(loadedClassificationRules));
    }
}