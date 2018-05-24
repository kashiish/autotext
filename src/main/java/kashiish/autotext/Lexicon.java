package main.java.kashiish.autotext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Lexicon {
	
	private HashSet<String> lexicon;;
	
	public Lexicon(String fileName) throws IOException {
		BufferedReader rd = openFile(fileName);
		this.lexicon = new HashSet<String>();
		readFile(rd);
	}
	
	public HashSet<String> getLexicon() {
		return this.lexicon;
	}
	
	public boolean containsWord(String word) {
		return this.lexicon.contains(word);
	}
	
	/*
	 * Reads all the lines (words) from the input reader and adds them 
	 * to `lexicon` HashSet. Closes the reader at the end of the file.
	 * @param rd 	BufferedReader for the input file
	 */
	private void readFile(BufferedReader rd) throws IOException {
		try {
			
			while(true) {
				String word = rd.readLine();
				if(word == null) break;
				lexicon.add(word.toLowerCase());
				
			}
			
			rd.close();
			
		} catch (IOException ex) {
			throw ex;
		}
		
	}


	/*
	 * Opens the input file for reading.
	 * @param	fileName		String, name of file
	 * @return 	BufferedReader
	 */
	private BufferedReader openFile(String fileName) throws IOException {
		BufferedReader rd = null;
		
		try {
			rd = new BufferedReader(new FileReader(fileName));
		} catch (IOException ex) {
			System.out.println("File not found: " + fileName);
			throw ex;
		}
		
		return rd;
	}



//	public static void main(String[] args) throws FileNotFoundException, IOException {
//		Lexicon lexicon = new Lexicon("sfsdf");
//	}

}
