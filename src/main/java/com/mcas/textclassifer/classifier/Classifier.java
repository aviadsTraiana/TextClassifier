package com.mcas.textclassifer.classifier;

import com.mcas.textclassifer.classifier.ds.Tag;
import com.mcas.textclassifer.classifier.ds.WordTrie;
import com.mcas.textclassifer.configurations.data.ClassificationRules;
import com.mcas.textclassifer.configurations.data.Rule;
import com.mcas.textclassifer.tokenizers.Token;
import com.mcas.textclassifer.tokenizers.TokenType;
import lombok.val;
import lombok.var;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Classifier {

    private final WordTrie trie;
    public Classifier(ClassificationRules classificationRules) {
        trie = new WordTrie();
        buildTrie(classificationRules);
    }

    private void buildTrie(ClassificationRules classificationRules) {
        for (Rule r:classificationRules.getRules()) {
            val tag = r.getDomain();
            r.getIndicators()
                    .stream()
                    .map(s -> s.split("\\s+"))
                    .forEach(words -> trie.addWords(words,tag));
        }
    }

    /**
     * Given a stream of tokens, will classify them into set of tags.
     * @param tokens stream of tokens
     * @return set of tags which were classified to all the tokens
     */
    public Set<Tag> classifyTokens(Stream<Token> tokens){
        //window represent the tokens we read along the trie, as we walk the trie
        val window = new LinkedList<String>();
        HashSet<Tag> result = new HashSet<>();
        tokens
                .filter(getTokenPredicate())
                .map(Token::getValue)
                .forEach(token -> {
                    val node = trie.readNext(token);
                    if(node.isPresent()){
                        val n =node.get();
                        if(!trie.isChildOfRoot()){ //we buffer in window words that may be used again
                            window.addLast(token);
                        }
                        if(n.isTagged()){
                            System.out.println("adding tag for "+token);
                            result.addAll(n.getTags());
                        }
                    }else{
                        if(!window.isEmpty()) {
                            window.addLast(token);
                            walkOnWindow(window, result);
                        }else{
                            trie.reset();
                        }
                    }
                });
        return result;
    }

    /**
     * walk through the window to see if we missed any token which is part of another suffix along the chain
     * @param window the window chain of nodes
     * @param result the tags to add if we find any
     */
    private void walkOnWindow(LinkedList<String> window, HashSet<Tag> result) {
        trie.reset();
        Optional<WordTrie.Node> node;
        for (String token : window) {
            node = trie.readNext(token);
            if (node.isPresent()) {
                val n = node.get();
                if(n.isTagged()){
                    System.out.println("adding tag for "+token);
                    result.addAll(n.getTags());
                }
            }else{
                break;
            }
        }
        if(!window.isEmpty()) window.removeFirst();
        if(!window.isEmpty()) walkOnWindow(window, result);
    }

    protected Predicate<Token> getTokenPredicate() {
        return t -> t.getType().equals(TokenType.WORD) || t.getType().equals(TokenType.NUMBER);
    }

}
