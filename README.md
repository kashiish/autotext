# AutoText

AutoText is a small autocomplete and autocorrect program written in Java 8. I wanted to create this project to use and understand new data structures. 

A [Trie](https://en.wikipedia.org/wiki/Trie) is an efficient data structure for prefix searching, therefore, it is widely used in autocomplete programs. 

A [BK-tree](https://en.wikipedia.org/wiki/BK-tree) data structure is a metric tree designed for efficient string matching, making it a logical structure for autocorrect. In my implementation, I used [Damerau–Levenshtein distance](https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance) to calculate edit distance between strings. 

Additionally, I created a simple GUI using Java Swing to go along with the program. I used a list from [@first20hours](https://github.com/first20hours/google-10000-english) (20k.txt) to create the lexicons for the tree structures (I have not uploaded that file into this repo).

<p align="center">
  <img src="https://github.com/kashiish/AutoText/blob/master/autocomplete.png?raw=true"/>
</p>

<p align="center">
	<img src="https://github.com/kashiish/AutoText/blob/master/autocorrect.png?raw=true"/>
</p>

## Usage

Please look at the javadoc comments for a more detailed usage description.

Here is an example of basic usage.

```java
import main.java.kashiish.autotext.AutoText;

import java.util.ArrayList;
```

```java
public static void main(String[] args) { 

	//Create a new AutoText instance with a file of words to build a Trie, BKTree, 
	//and lexicon for word validation
	AutoText autotext = new AutoText(lexiconFileName);
	/*
	 * Or you can use different dictionaries for each set up.
	 * AutoText autotext = new AutoText(lexiconFileName, trieFileName, bktreeFileName);
	 */
	ArrayList<String> corrections = autotext.autocorrect("lovly");
	//set max autocomplete suggestions
	at.setMaxSuggestions(3);
	ArrayList<String> suggestions = autotext.autocomplete("ques")
	System.out.println(corrections);
	System.out.println(suggestions);
}
```

```
["lovely"]
["quest", "question", "questions"]
```

## Issues and Contribution

Please feel free to report or fix any bugs you may find in the program. It's greatly appreciated!

### License

MIT