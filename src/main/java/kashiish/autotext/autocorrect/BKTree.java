package main.java.kashiish.autotext.autocorrect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import main.java.kashiish.autotext.Lexicon;
import main.java.kashiish.autotext.Tree;

/**
 * BKTree data structure.
 * @author kashish
 *
 */
public class BKTree implements Tree {
	
	/* The maximum edit distance a word can have from input word to be considered a similar word/autocorrect word. */
	private static final int TOLERANCE = 3;
	/* The maximum number of matches to get while searching for similar words. */
	private static final int MAX_MATCHES = 10;
	/* The maximum number of suggestions to return when {@link #getClosestWords(String)} is called. */
	private int maxSuggestions = 3;
	/* Root of tree. */
	private TreeNode root;
	/* The number of words in the tree. */
	private int size = 0;

	
	/**
	 * Creates a new BKTree. 
	 * @param lexicon		Lexicon, words to add to the BKTree
	 * @throws IOException
	 */
	public BKTree(Lexicon lexicon) throws IOException {
		createTree(lexicon);
	}
	
	/**
	 * Creates a new BKTree object with specified file of words to add to the tree. 
	 * @throws IOException
	 */
	public BKTree(String fileName) throws IOException {
		createTree(new Lexicon(fileName));
	}
	
	
	/**
	 * Inserts a new word into the BKTree.
	 * @param word		String
	 */
	@Override
	public void insertWord(String word) {
		
		if(word == null || word.length() == 0)
			throw new IllegalArgumentException("String is not valid.");
		
		//remove everthing except letters
		word = word.toLowerCase().replaceAll("[^a-z]", "");
				
		if(root == null) {
			root = new TreeNode(word);
			this.size++;
			return;
		}
		
		TreeNode current = root;
			
		int distance = getDistance(word, current.getValue());
		
		while(current.getChild(distance) != null && distance != 0) {
			current = current.getChild(distance);
			distance = getDistance(word, current.getValue());
		}
		
		current.addChild(new TreeNode(word, distance));
		this.size++;
	}
	
	@Override
	public int getNumWords() {
		return this.size;
	}
	
	/**
	 * Sets the maximum number of autocorrected words to return. 
	 * @param matches		int 
	 */
	@Override
	public void setMaxMatches(int matches) {
		this.maxSuggestions = matches;
	}
	
	/**
	 * Gets a list of words from the BKTree that have the shortest edit distance from 
	 * the input word. 
	 * @param word				String
	 * @return ArrayList<String>	
	 */
	public ArrayList<String> getClosestWords(String word) {
		//Get closest words (edit distance within TOLERANCE)
		HashSet<CloseWord> closeWords = getCloseWords(word);
		
		//The shortest distance found within `closeWords`
		int shortestDistance = Integer.MAX_VALUE;
		
		//All the words with the `shortestDistance`
		ArrayList<String> closestWords = new ArrayList<String>();
			
		//find the shortest edit distance within `closeWords`
		for(CloseWord closeWord : closeWords) {
			if(closeWord.distance < shortestDistance)
				shortestDistance = closeWord.distance;
		}
		
		//Get all the words with the `shortestDistance`
		for(CloseWord closeWord : closeWords) {
			if(closeWord.distance == shortestDistance) closestWords.add(closeWord.word);
			if(closestWords.size() == maxSuggestions) break;
		}
		
		return closestWords;
	}
	
	/* CloseWord class contains a word and its distance from the queried word. */
	private class CloseWord {
		String word;
		int distance;
		
		public CloseWord(String word, int distance) {
			this.word = word;
			this.distance = distance;
		}
		
	}
	
	/*
	 * Gets a set of the words that have edit distance within the specified tolerance (static variable)
	 * from the input word.
	 * @param word					String
	 * @return HashSet<String>
	 */
	private HashSet<CloseWord> getCloseWords(String word) {
		HashSet<CloseWord> closeWords = new HashSet<CloseWord>();
		if(word.equals("")) return closeWords;
		
		getCloseWords(closeWords, root, word.toLowerCase().replaceAll("[^a-z]", ""));
			
		return closeWords;
		
	}
	
	/*
	 * Recursive helper function that traverses the root's children that have edit distance
	 * within the range of `TOLERANCE` from the input `word`. Adds root value to 
	 * `closeWords` if edit distance is less than or equal to <TOLERANCE>.
	 * @param closeWords		HashSet<String>
	 * @param root 				TreeNode
	 * @param word				String
	 */
	private void getCloseWords(HashSet<CloseWord> closeWords, TreeNode root, String word) {
		if(root == null) return;
		if(closeWords.size() == MAX_MATCHES) return;
		
		int distance = getDistance(word, root.getValue());
		int minDist = (distance - TOLERANCE);
		int maxDist = (distance + TOLERANCE);
				
		if(distance <= TOLERANCE)
			closeWords.add(new CloseWord(root.getValue(), distance));
		
		for(int i = minDist; i <= maxDist; i++) {
			TreeNode node = root.getChild(i);
			if(node != null)
				getCloseWords(closeWords, node, word);
		}
		
	}
	
	/*
	 * Calculates the edit distance between two strings using Damerau-Levenshtein's Distance weighted for
	 * QWERTY keyboard. 
	 * @param wordA		String
	 * @param wordB		String
	 * @return double	Edit distance between input strings
	 */
	private int getDistance(String wordA, String wordB) {
		
		if(wordA.length() == 0 || wordB.length() == 0) return Math.max(wordA.length(), wordB.length());
		
		int m = wordA.length();
		int n = wordB.length();
		
		int[][] memo = new int[m + 1][n + 1];

		getDistanceUtil(wordA, wordB, m, n, memo);
		
		return memo[m][n];
	}
	
	/*
	 * Helper recursive function for {@link #getDistance(String, String)}.
	 * @param wordA			String		
	 * @param wordB			String
	 * @param i				int, index of current character in wordA
	 * @param j				int, index of current character in wordB
	 * @param memo			double[][], used to store previously calculated results
	 * @return double		edit distance between substrings wordA[i:m] and wordB[j:n]
	 */
	private int getDistanceUtil(String wordA, String wordB, int i, int j, int[][] memo) {
		if(i == 0 && j == 0) return 0;
		if(i == 0 || j == 0) return Math.max(i, j) + 2;
		if(memo[i][j] != 0) return memo[i][j];
		
		if(wordA.charAt(i - 1) == wordB.charAt(j - 1))
			return memo[i][j] = getDistanceUtil(wordA, wordB, i - 1, j - 1, memo);
		
		boolean keysAreClose = keysAreClose(wordA.charAt(i - 1), wordB.charAt(j - 1));
		boolean isTransposed = isTransposed(wordA, wordB, i, j);
		int substitutionWeight = !keysAreClose && isTransposed ? -2 : (keysAreClose ? 0 : 1) + (isTransposed ? 0 : 1);
		
		
		int substitute = getDistanceUtil(wordA, wordB, i - 1, j - 1, memo) + substitutionWeight;
		/* 
		 * Adds one to the value if the previous character is not the same as current character and
		 * subtracts one if it is the same.
		 * Helps decrease the edit distance if a character is typed consecutively or is missing a repeated letter.
		 * For example "catle" -> "cattle" or "mapple" -> "maple"
		 */
		int insert = getDistanceUtil(wordA, wordB, i, j - 1, memo) + (samePreviousChar(wordB, j) ? -1 : 1);
		int delete = getDistanceUtil(wordA, wordB, i - 1, j, memo) + (samePreviousChar(wordA, i) ? -1 : 1);
		return memo[i][j] = Math.min(substitute, Math.min(insert, delete)) + 1;

	}
			
	/*
	 * Checks if the previous character before index k in the word is the same as 
	 * the character at index k. 
	 * @param word		String	
	 * @param k			int, index of current character
	 * @return boolean
	 */
	private boolean samePreviousChar(String word, int k) {
		if(word.length() < 3) return false;
		if(k == word.length()) {
			return word.charAt(k - 1) == word.charAt(k - 2);
		} else {
			return word.charAt(k) == word.charAt(k - 1);
		}
	}
	
	/*
	 * Determines if a character from one string is transposed in the other.
	 * @param wordA			String
	 * @param wordB			String
	 * @param i				int, index of wordA character
	 * @param j				int, index of wordB character
	 * @return boolean
	 */
	private boolean isTransposed(String wordA, String wordB, int i, int j) {
		return i > 1 && j > 1 && wordA.charAt(i - 2) == wordB.charAt(j - 1) && wordA.charAt(i - 1) == wordB.charAt(j - 2);
	}
	
	/*
	 * This function determines if two keys (chars) are close to each other on a QWERTY
	 * keyboard. 
	 * Thank you to @scott @StackOverflow https://stackoverflow.com/questions/7079183/how-to-check-efficiently-if-two-characters-are-neighbours-on-the-keyboard
	 * @param a				char
	 * @param b				char
	 * @return boolean
	 */
	private boolean keysAreClose(char a, char b) {
		/* 
		 * Assign numbers to each character determined by the key position on qwerty keyboard.
		 * The first row has 10 letters and middle row has 9. 10 - 9 = 1, so the middle row characters have an additional 0.1 added.
		 * The last row has 7 letters, so last row characters have an additional 0.3 added.
		 */
		double[] chars = new double[]{10.1, 24.3, 22.3, 12.1, 2, 13.1, 14.1, 15.1, 7, 16.1, 17.1, 18.1, 
									  26.3, 25.3, 8, 9, 0, 3, 11.1, 4, 6, 23.3, 1, 21.3, 5, 20.3};

		double one = chars[a - 97];
		double two = chars[b - 97];
				
		double val = Math.abs(one - two);
				
		return ((double) Math.round(val * 10) / 10 == 1 || val > 9 && val <= 11);
		
	}

}
