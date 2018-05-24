package main.java.kashiish.autotext.autocorrect;

import java.util.HashMap;

import main.java.kashiish.autotext.Node;

/**
 * Node representation for BKTree data structure.
 * @author kashish
 *
 */
public class TreeNode implements Node<String, TreeNode, Integer> {
	
	/* TreeNode's data */
	private String value;
	/* this.value's edit distance from parent node's value */
	private Integer distance;
	/* Collection of TrieNode's children */
	private HashMap<Integer, TreeNode> children; 
	
	/** 
	 * Creates a new TreeNode object with specified string. 
     * @param s		String
     */
	public TreeNode(String s) {
		this.value = s;
		this.children = new HashMap<Integer, TreeNode>();
	}
	
	/** 
	 * Creates a new TreeNode object with specified string and edit distance from parent node. 
     * @param s				String
     * @param distance		Double
     */
	public TreeNode(String s, Integer distance) {
		this.value = s;
		this.distance = distance;
		this.children = new HashMap<Integer, TreeNode>();
	}
	
	public int getDistanceFromParent() {
		return this.distance;
	}


	/**
	 * Gets the TreeNode's value. 
	 * @return String	TreeNode's value
	 */
	@Override
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Gets the TreeNode's children collection.
	 * @return HashMap<Integer, TreeNode>
	 */
	@Override
	public HashMap<Integer, TreeNode> getChildren() {
		return this.children;
	}

	/**
	 * Gets the child with the specified distance from this TreeNode.
	 * @param distance		Integer, node's distance from this TreeNode
	 * @return TreeNode
	 */
	@Override
	public TreeNode getChild(Integer distance) {
		return this.children.get(distance);
	}
	
	
	/**
	 * Adds a child to TreeNode's collection of children.
	 * @param TreeNode		 child to be added into the children collection
	 */
	@Override
	public void addChild(TreeNode child) {
		this.children.put(child.getDistanceFromParent(), child);
		
	}
	
	public String toString() {
		return this.value;
	}

}
