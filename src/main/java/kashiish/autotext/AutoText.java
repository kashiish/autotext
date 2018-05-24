package main.java.kashiish.autotext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import main.java.kashiish.autotext.autocomplete.Trie;
import main.java.kashiish.autotext.autocorrect.BKTree;

/**
 * Program that performs autocorrect and autocomplete on Strings.
 * @author kashish
 *
 */
public class AutoText {
	
	private Lexicon lexicon;
	private Trie trie;
	private BKTree bktree;

	
	/**
	 * Creates a new AutoText object with specified file name to create a lexicon with.
	 * The lexicon will be used to for autocomplete, autocorrect, and validating words. 
	 * @param fileName
	 * @throws IOException
	 */
	public AutoText(String lexiconFile) throws IOException {
		this.lexicon = new Lexicon(lexiconFile);
		this.trie = new Trie(this.lexicon);
		this.bktree = new BKTree(this.lexicon);

	}
	
	/**
	 * Creates a new AutoText object with specified file to create a lexicon with, file to
	 * create Trie with, and file to create BKTree with.
	 * @param lexiconFile		String
	 * @param trieFileName		String
	 * @param bkTreeFileName	String
	 * @throws IOException
	 */
	public AutoText(String lexiconFile, String trieFileName, String bktreeFileName) throws IOException {
		this.lexicon = new Lexicon(lexiconFile);
		this.trie = new Trie(trieFileName);
		this.bktree = new BKTree(bktreeFileName);
	}
	
	/**
	 * If the input word is an invalid word (not found in lexicon), gets closest
	 * words (shortest edit distance) to the input word. If word is valid or no words
	 * are found (ex. word is not in the BKTree lexicon or the if the input word had 
	 * an edit distance greater than the specified tolerance), returns null.
	 * @param word					String
	 * @return ArrayList<String> 
	 */
	public ArrayList<String> autocorrect(String word) {
		if(isValidWord(word)) return null;
		ArrayList<String> words = bktree.getClosestWords(word);
		if(words.size() == 0) return null;
		return words;
	}
	
	/**
	 * Gets a list of words that have the given prefix and sorts them based on String length. 
	 * If no words were found in the Trie, an empty set will be returned. 
	 * @param prefix			String
	 * @return ArrayList<String>
	 */
	public ArrayList<String> autocomplete(String prefix) {
		ArrayList<String> result = trie.getWordsWithPrefix(prefix);
		Collections.sort(result, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.length() - o2.length();
			}
			
		});
		
		return result;
	}
	
	/**
	 * Checks if the given word is in the lexicon. 
	 * @param word		String
	 * @return boolean
	 */
	public boolean isValidWord(String word) {
		return lexicon.containsWord(word);
	}
	
	/**
	 * Sets the maximum number of auto-suggestions to return.
	 * @param numSuggestions		int
	 */
	public void setMaxSuggestions(int numSuggestions) {
		trie.setMaxMatches(numSuggestions);
	}
	
	/**
	 * Sets the maximum number of autocorrected words to return.
	 * @param numCorrections		int
	 */
	public void setMaxCorrections(int numCorrections) {
		bktree.setMaxMatches(numCorrections);
	}
	
	/**
	 * Adds words from a specific file name to Autocomplete program (Trie).
	 * @param fileName
	 * @throws IOException 
	 */
	public void addWordsFromFileAutocomplete(String fileName) throws IOException {
		trie.addWordsFromFile(fileName);
	}
	
	/**
	 * Adds words from a specific file name to Autocorrect program (BKTree).
	 * @param fileName
	 * @throws IOException 
	 */
	public void addWordsFromFileAutocorrect(String fileName) throws IOException {
		bktree.addWordsFromFile(fileName);
	}
	

}
