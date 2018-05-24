package test.java.kashiish.autotext;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.kashiish.autotext.autocorrect.BKTree;


public class TestBKTree {
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	private BKTree bktree;
	
	@Before
	public void setUpTrie() throws IOException {
		bktree = new BKTree("src/test/resources/test_words_bk.txt");	
	}
	
	@Test
	public void testTrieCreation() {
		//the number of words in test_words.txt is 3503
		assertEquals(3503, bktree.getNumWords());
	}
	
	@Test
	public void testInvalidInsertions() {
		thrown.expect(IllegalArgumentException.class);
		bktree.insertWord(null);
		bktree.insertWord("");
	}
	
	@Test
	public void testClosestWords() {
		//transposition
		testCloseWordsHelper("recieve", new String[]{"receive"});
		//missing character
		testCloseWordsHelper("whre", new String[]{"where"});
		//missing character + substitution 
		//"amazin" could correct to "amazon" because 'i' is close to 'o' on qwerty keyboard
		testCloseWordsHelper("amazin", new String[]{"amazon", "amazing"});
		//missing consecutive character
		testCloseWordsHelper("gogle", new String[]{"google"});
		testCloseWordsHelper("asessment", new String[]{"assessment"});
		//substitution
		//should return "there" but not "where" because 'y' is closer to 't' than 'w' on qwerty keyboard
		testCloseWordsHelper("yhere", new String[]{"there"});
		//deletion
		testCloseWordsHelper("utillities", new String[]{"utilities"});
		testCloseWordsHelper("janurary", new String[]{"january"});
	}
	
	@Test
	public void testClosestWordsPunctuation() {
		bktree.insertWord("etc.");
		assertEquals(true, bktree.getClosestWords("etc.").contains("etc"));
		bktree.insertWord("make up");
		assertEquals(true, bktree.getClosestWords("make up").contains("makeup"));
	}
	
	private void testCloseWordsHelper(String word, String[] expected) {
		assertEquals(new HashSet<>(Arrays.asList(expected)), new HashSet<>(bktree.getClosestWords(word)));
	}

}
