package com.mcas.textclassifier.classifier.ds;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

class WordTrieTest {

    WordTrie trie;
    @BeforeEach
    void setUp() {
        trie= new WordTrie();
        trie.addWords(new String[]{"bank","account"},"financial");
        trie.addWords(new String[]{"bank","account"},"fintech");
        trie.addWords(new String[]{"bank","manager"},"financial");
        trie.addWords(new String[]{"medical","insurance"},"healthcare");
    }

    @Test
    void testHealthcareExist(){
        trie.readNext("medical");
        val insuranceNode = trie.readNext("insurance").map(i->i.getTags().stream().map(Tag::getValue).collect(Collectors.toSet()));
        assertThat("insurance is not a child of medical after resetting", insuranceNode.isPresent());
        assertThat(insuranceNode.get(), containsInAnyOrder("healthcare"));
    }

    @Test
    void testNonExistingWord(){
        val bankNode = trie.readNext("bank");
        assertTrue(bankNode.isPresent());
        assertFalse(trie.readNext("aviad").isPresent());
    }

    @Test
    void testMultiTags() {
        val bankNode = trie.readNext("bank");
        assertTrue(bankNode.isPresent());
        val accountNode = trie.readNext("account");
        assertTrue(accountNode.isPresent());
        assertTrue(accountNode.get().isTagged());
        val tags = accountNode.get().getTags().stream().map(Tag::getValue).collect(Collectors.toSet());
        assertThat(tags,containsInAnyOrder("financial","fintech"));
    }

    @Test
    void isChildOfRoot() {
        trie.readNext("bank");
        assertTrue(trie.isFirstWord(),()->"bank should be child of root");
        trie.readNext("account");
        assertFalse(trie.isFirstWord(),()->"account should not be the child of root");
        trie.reset();
        assertFalse(trie.isFirstWord(),()->"after restart we have not read yet , so we are not at first node");

    }

    @Test
    void testNodeUniqueness(){
        val bankNode1 = trie.readNext("bank");
        assertTrue(trie.readNext("account").isPresent());
        trie.reset();
        val bankNode2 = trie.readNext("bank");
        assertEquals(bankNode2, bankNode1);
        val managerNode = trie.readNext("manager");
        assertTrue(managerNode.isPresent());
    }



}