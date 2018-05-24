package test.java.kashiish.autotext;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.kashiish.autotext.autocomplete.Trie;


public class TestTrie {
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	private Trie trie;
	
	@Before
	public void setUpTrie() throws IOException {
		trie = new Trie("src/test/resources/test_words_trie.txt");	
	}
	
	@Test
	public void testTrieCreation() {
		//the number of words in test_words.txt is 3836
		assertEquals(3836, trie.getNumWords());
	}
	
	@Test
	public void testInvalidInsertions() {
		thrown.expect(IllegalArgumentException.class);
		trie.insertWord(null);
		trie.insertWord("");
	}
	
	
	@Test
	public void testPrefixSuggestionsNoSpaces() {
		testPrefixSuggestions("wri", new String[]{"wrist", "write", "write back", "write down", "writer", "writing", "written"});
		testPrefixSuggestions("far", new String[]{"far", "farm", "farmer", "farming", "farther", "farthest"});
		testPrefixSuggestions("z", new String[]{"zero", "zone"});

	}
	
	@Test 
	public void testPrefixSuggestionsWithSpaces() {
		testPrefixSuggestions("give ", new String[]{"give away", "give back", "give in", "give off", "give out", "give up"});
		testPrefixSuggestions("hang a", new String[]{"hang about", "hang about with", "hang around", "hang around with"});
	}
	
	
	@Test
	public void testMaxResults() {
		int maxMatches = 6;
		trie.setMaxMatches(maxMatches);
		assertEquals(maxMatches, trie.getWordsWithPrefix("b").size());
		
		maxMatches = 20;
		trie.setMaxMatches(maxMatches);
		assertEquals(maxMatches, trie.getWordsWithPrefix("a").size());
	}
	
	private void testPrefixSuggestions(String prefix, String[] expected) {
		assertEquals(new HashSet<>(Arrays.asList(expected)), new HashSet<>(trie.getWordsWithPrefix(prefix)));
	}


}
