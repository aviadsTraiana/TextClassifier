package com.mcas.textclassifier.classifier.ds;

import lombok.Getter;

import java.util.*;

/**
 * WordTrie is a kind of prefix tree, but of words instead of characters.
 * it is only thread safe for read, not write.
 * so you can do your preprocessing by adding all the words to it with `addWords` sequentially
 * and then read from it in parallel with `readNext`.
 */
public class WordTrie {
    public static class Node{
        @Getter private final Node parent;
        @Getter private Set<Tag> tags;
        Map<String,Node> children;

        public Node(Node parent) {
            this.parent=parent;
            children = new Hashtable<>();
        }
        public void addTag(Tag tag){
            if(tags==null){
                tags= new HashSet<>();
            }
            tags.add(tag);
        }
        public boolean isTagged(){
            return tags!=null && !tags.isEmpty();
        }
    }
    private final Node root;
    private final Map<String,Tag> tags;
    private final ThreadLocal<Node> currentNode;
    //private Node current;

    public WordTrie() {
        this.root = new Node(null);
        //this.current = root;
        currentNode = new ThreadLocal<>();
        currentNode.set(root);
        this.tags = new Hashtable<>();
    }

    /**
     * the method add all the words to the Word Trie,
     * at the leaf the tag will be available.
     *
     * @param words a collection of ordered words, aka sentence.
     * @param tag the tag that classifies the chain of words.
     */
    public void addWords(String[] words,String tag){
        Tag t = tags.get(tag);
        if(t==null){
            t = new Tag(tag);
            tags.put(tag,t);
        }
        Node currNode = root;
        int i=0;
        int lastWord = words.length-1;
        for (String word:words) {
            Node n = currNode.children.get(word);
            if(n==null){
                n = new Node(currNode);
                if(i==lastWord){
                    n.addTag(t);
                }
                currNode.children.put(word,n);
            }else{
                if(i==lastWord){
                    n.addTag(t);
                }
            }
            currNode = n;
            i++;
        }
    }
    public boolean isFirstWord(){
        //return current.parent==root;
        return currentNode.get().parent==root;
    }
    public Optional<Node> readNext(String word){
       //return readFrom(this.current, word);
       return readFrom(this.currentNode.get(), word);
    }
    public Optional<Node> readFrom(Node node, String word){
        if(node!=null) {
            //this.current = node.children.get(word);
            this.currentNode.set(node.children.get(word));
            //return Optional.ofNullable(this.current);
            return Optional.ofNullable(this.currentNode.get());
        }
        return Optional.empty();
    }

    public void reset(){
        //this.current = root;
        this.currentNode.set(root);
    }
}
