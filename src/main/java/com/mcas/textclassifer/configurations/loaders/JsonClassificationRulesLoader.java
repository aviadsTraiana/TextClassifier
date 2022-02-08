package com.mcas.textclassifer.configurations.loaders;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mcas.textclassifer.configurations.data.ClassificationRules;
import com.mcas.textclassifer.configurations.data.Rule;

import java.io.Reader;
import java.util.List;

public class JsonClassificationRulesLoader implements ClassificationRulesLoader {

    public static final String CLASSIFICATION_RULES_JSON_NAME = "classification_rules";

    @Override
    public ClassificationRules load(Reader reader) {
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        JsonArray listOfRules = jsonObject.getAsJsonArray(CLASSIFICATION_RULES_JSON_NAME);
        Gson g = new Gson();
        return new ClassificationRules(g.fromJson(listOfRules, new TypeToken<List<Rule>>() {}.getType()));
    }
}
