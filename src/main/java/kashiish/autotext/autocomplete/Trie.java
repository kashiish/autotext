package main.java.kashiish.autotext.autocomplete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import main.java.kashiish.autotext.Lexicon;
import main.java.kashiish.autotext.Tree;

/**
 * Trie data structure.
 * @author kashish
 *
 */
public class Trie implements Tree {
	
	/* The maximum number of matches to get while traversing the tree. */
	private int maxMatches = 10;
	/* Root of trie. */
	private TrieNode root;
	/* The number of words in the trie. */
	private int numWords = 0;
	
	/**
	 * Creates a new Trie object.
	 * @param lexicon 		Lexicon, words to add to the Trie
	 * @throws IOException 
	 */
	public Trie(Lexicon lexicon) throws IOException {
		this.root = new TrieNode();
		createTree(lexicon);
	}
	
	/**
	 * Creates a new Trie object with specified file of words to add to the tree.
	 * @param fileName 		String
	 * @throws IOException 
	 */
	public Trie(String fileName) throws IOException {
		this.root = new TrieNode();
		createTree(new Lexicon(fileName));
	}

	
	/**
	 * Inserts a new word into the Trie.
	 * @param word		String, the word to be inserted
	 */
	@Override
	public void insertWord(String word) {
		
		if(word == null || word.length() == 0)
			throw new IllegalArgumentException("String is not valid.");
		
		
		word = word.toLowerCase();
		
		TrieNode current = root;	
		
		int length = word.length();
		
		for(int i = 0; i < length; i++) {
			char c = word.charAt(i);
			if(current.getChild(c) == null)
				current.addChild(new TrieNode(c));
			
			current = current.getChild(c);
		}
		current.setLeaf(true);
		this.numWords++;
	}
	
	
	@Override
	public int getNumWords() {
		return this.numWords;
	}
	
	/**
	 * Sets the number of suggestions to return. 
	 * @param matches		int
	 */
	@Override
	public void setMaxMatches(int matches) {
		this.maxMatches = matches;
	}
	
	/**
	 * Gets words with given prefix (including `prefix`, if it is a valid word). 
	 * @param prefix				String
	 * @return ArrayList<String>	A list of all words with prefix
	 */
	public ArrayList<String> getWordsWithPrefix(String prefix) {		
		ArrayList<String> wordsWithPrefix = new ArrayList<String>();
		
		if(prefix == null || prefix.length() == 0) return wordsWithPrefix;
		
		prefix = prefix.toLowerCase();
		
		int length = prefix.length();
	        
        TrieNode current = root;
        int i = 0;
        
        //get to the last character of the prefix 
        for(;i < length; i++) {
        	char c = prefix.charAt(i);
            TrieNode node = current.getChild(c);
            if(node == null) return wordsWithPrefix;
            current = node;
        }
        
        getWordsWithPrefix(current, new StringBuilder(prefix), wordsWithPrefix);
        
        return wordsWithPrefix;
        
	}
	
	/*
	 * Traverses the Trie and adds new words into the result list if the current TrieNode is a leaf node.
	 * Stops traversing the Trie once the maximum number of matches have been added to the list or
	 * there are no more nodes left to traverse.
	 * @param root			TrieNode, current node
	 * @param word			StringBuilder
	 * @param result		ArrayList<String>, list that will contain all words with given prefix
	 */
	private void getWordsWithPrefix(TrieNode root, StringBuilder word, ArrayList<String> result) {
		if(root == null || result.size() == maxMatches) return;
				
		if(root.isLeaf()) result.add(String.valueOf(word));
		
		for (Entry<Character, TrieNode> entry : root.getChildren().entrySet()) {
			updateWord(entry.getValue(), word, result);
		}
		
	}
	
	/*
	 * Adds the current node's character onto `prefix` and calls `getWordsWithPrefix` to continue
	 * traversing the tree and forming the full word.
	 * @param root			TrieNode, current node
	 * @param prefix		StringBuilder
	 * @param result		ArrayList<String>
	 */
	private void updateWord(TrieNode node, StringBuilder prefix, ArrayList<String> result) {		
		StringBuilder newPrefix = new StringBuilder(prefix);
		
		newPrefix.append(node.getValue());
		
		getWordsWithPrefix(node, newPrefix, result);
		
	}

}
