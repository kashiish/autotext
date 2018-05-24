package main.java.kashiish.autotext.autocomplete;

import java.util.HashMap;

import main.java.kashiish.autotext.Node;

/**
 * Node representation for Trie data structure. 
 * @author kashish
 *
 */
public class TrieNode implements Node<Character, TrieNode, Character> {
	
	/* TrieNode's data */
	private Character value;
	/* Collection of TrieNode's children */
	private HashMap<Character, TrieNode> children; 
	
	/* TrieNode leaf property */
    private boolean isLeaf = false;
  
    /** Creates a new TrieNode object. */
    public TrieNode() {
    	this.children = new HashMap<Character, TrieNode>();
    }

    /** Creates a new TrieNode object with specified character. 
     * @param c			Character
     */
    public TrieNode(Character c) {
        this.value = c;
        this.children = new HashMap<Character, TrieNode>();
    }
    
    /**
     * Returns whether or not TrieNode is a leaf.
     * @return boolean		TrieNode leaf property
     */
    public boolean isLeaf() {
    	return this.isLeaf;
    }
    
    /**
     * Sets the isLeaf property of TrieNode.
     * @param value		boolean, value of isLeaf property
     */
    public void setLeaf(boolean value) {
    	this.isLeaf = value;
    }
    
    /** 
	 * Gets the TrieNode's value. 
	 * @return Character	TrieNode's value
	 */
	@Override
	public Character getValue() {
		return this.value;
	}
	
	/**
	 * Gets the TrieNode's children collection.
	 * @return HashMap<Character, TrieNode>
	 */
	@Override
	public HashMap<Character, TrieNode> getChildren() {
		return this.children;
	}

	/**
	 * Gets the child with the specified character.
	 * @return TrieNode		child
	 */
	@Override
	public TrieNode getChild(Character c) {
		return this.children.get(c);
	}
	
	/**
     * Adds a child to TrieNode's collection of children.
     * @param child		TrieNode, child to be added into the children collection
     */
	@Override
	public void addChild(TrieNode child) {
		this.children.put(child.getValue(), child);
	}


}
