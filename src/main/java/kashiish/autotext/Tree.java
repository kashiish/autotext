package main.java.kashiish.autotext;

import java.io.IOException;
import java.util.HashSet;

public interface Tree {
	
	void insertWord(String word);

	void setMaxMatches(int matches);
	
	/**
	 * Adds all the words from a file to the Tree.
	 * @param fileName					String
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	default void addWordsFromFile(String fileName) throws IOException {
		createTree(new Lexicon(fileName));
	};
	
	 /**
	  * Returns the number of words in the tree.
	  * @return int
	  */
	 int getNumWords();
	
	/*
	 * Creates a new Lexicon and inserts all the words into the Tree.
	 * @param lexicon					Lexicon, words to add to the tree
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	 default void createTree(Lexicon lexicon) throws IOException {
		HashSet<String> words = lexicon.getLexicon();
		for(String word : words) {
			insertWord(word);
		}
	 }
	 
}
