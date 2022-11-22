package predictive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used to store all words in dictionary for a faster search and lookup
 * @author Hans Sean Nathanael
 *
 */
public class DictionaryListImpl {
	
	// this array signatureOfChar used to simplify function wordToSignature
	// The length is 26 representing all the alphabet a-z
	// index 0 is for 'a', index 1 for 'b', and so on until last index (index 25) for 'z'
	private char signatureOfChar[] = {'2', '2', '2',
									  '3', '3', '3',
									  '4', '4', '4',
									  '5', '5', '5',
									  '6', '6', '6',
									  '7', '7', '7', '7',
									  '8', '8', '8',
									  '9', '9', '9', '9'};
	
	// listDictionaryWords used to store all words that have been read from dictionary with it's signature
	private List<WordSig> listDictionaryWords = new ArrayList<WordSig>();
	
	/**
	 * Constructor of this class 
	 * all words in dictionary file will be stored in listDictionaryWords
	 */
	public DictionaryListImpl()
	{
		
		// store all words in dictionary to list dictionary
		try {
			
			// fileReader used to read every line of dictionary file
			// BufferedReader is faster than Scanner when read file line one by one
			BufferedReader fileReader = new BufferedReader(new FileReader("assets/words"));
			
			// fileOneLine used to store every line read by fileReader
			String fileOneLine = null;
			
			// reading every line of dictionary
			while((fileOneLine = fileReader.readLine()) != null)
			{
				// changed to lower case for easier comparison and because
				// all words need to stored in lower case
				fileOneLine = fileOneLine.toLowerCase();
				
				// skip the word with non alphabet character(s)
				if (this.isValidWord(fileOneLine))
				{
					listDictionaryWords.add(new WordSig(this.wordToSignature(fileOneLine), fileOneLine));
				}
			}
			
			// don't forget to close the file
			fileReader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// listDictionaryWords must be sorted because method signatureToWords use binary search
		// to find a word from it's signature. But actually, it's very likely that the
		// listDictionaryWords already sorted because the dictionary file is very likely
		// already sorted too, but just in case this will be sorted using Collections.
		Collections.sort(listDictionaryWords);
	}
	
	/**
	 * Convert a string of word to a signature (number)
	 * "Hello" become "43556" not "4433555555666"
	 * This algorithm use array signatureOfChar rather than 'nested if'
	 * 'Nested if' make program longer, by this it will easier to read
	 * @param word : String of word that will be converted to its signature
	 * @return signature of converted word
	 */
	public String wordToSignature(String word)
	{
		// signatureStringBuffer used to make signature of the word
		// StringBuffer is used to make signature string rather than using String
		// because it is faster to append a string especially appending many times using loop
		// when append a string using '+', the string converted to StringBuffer and then
		// append the StringBuffer, then convert it back to String, it is faster
		// by just using StringBuffer, no need convert it to String every time it is appended
		StringBuffer signatureStringBuffer = new StringBuffer("");
		
		// word must not null to be converted
		if (word == null)
		{
			return word;
		}
		word = word.toLowerCase(); // to the word become lower case so it's easier to converted
		
		for(int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) >= 'a' && word.charAt(i) <= 'z')
			{
				signatureStringBuffer.append(signatureOfChar[word.charAt(i)- 'a' ]);
			}
			else
			{
				signatureStringBuffer.append(" "); // for non alphabet changed to " " (space)
			}
		}
		
		return signatureStringBuffer.toString();
	}
	
	/**
	 * Method to translate signature of word into a word that exist in listDictionaryWords
	 * @param signature : string of number that will translated to word
	 * @return Set (in form of HashSet) of String that contain all possible word in dictionary
	 */
	public Set<String> signatureToWords(String signature)
	{
		
		// all char of signature must be numeric
		if (isNumericWord(signature) && signature.length() > 0)
		{
			// Set containing all possible word of a signature that exist in dictionary
			// HashSet is used because it is fast (O(1), constant time lookup) because it use hashing
			// to choose where it will be stored or retrieved and using balance tree for collision
			// HashSet is faster than TreeSet (O(log n) time complexity because tree algorithm) and
			// LinkedHashSet (similar to HashSet but use LinkedList for collision)
			Set<String> matchedPossibleWord = new HashSet<String>();
			
			// search just by signature, the word of signature doesn't important in search algorithm
			WordSig searchKey = new WordSig(signature, "");
			int indexFound = Collections.binarySearch(listDictionaryWords, searchKey);
			
			// if indexFound < 0, that means the signature doesn't exist
			if (indexFound >= 0)
			{
				matchedPossibleWord.add(listDictionaryWords.get(indexFound).getWord());
				
				// because Collection binary search doesn't guarantee which value will
				// be retrieve if there's more than 1 same value, it will return the first one
				// it get (could be in the middle, not upper bound or lower bound)
				// so linear search used to find the all word with same signature
				// by search up and search down from the index of found object
				
				int walkerUp = indexFound + 1;
				int walkerDown = indexFound - 1;
				
				// search up
				while(walkerUp < listDictionaryWords.size() &&
					  listDictionaryWords.get(walkerUp).compareTo(searchKey) == 0)
				{
					matchedPossibleWord.add(listDictionaryWords.get(walkerUp).getWord());
					walkerUp += 1;
				}
				
				// search down
				while(walkerDown >= 0 &&
					  listDictionaryWords.get(walkerDown).compareTo(searchKey) == 0)
				{
					matchedPossibleWord.add(listDictionaryWords.get(walkerDown).getWord());
					walkerDown -= 1;
				}
			}
			
			return matchedPossibleWord;
		}
		
		// return empty set if the signature contain non numerical
		// or the signature is empty
		return new HashSet<String>();
	}
	
	/**
	 * Method to check if a string just contain alphabet characters
	 * @param word : string to be checked
	 * @return true if the word just contain alphabet (lower case), false if it contain non alphabet (lower case)
	 */
	private boolean isValidWord(String word)
	{
		for (int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) < 'a' || word.charAt(i) > 'z')
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method to check if a string contain non numerical
	 * @param word : string to be checked
	 * @return true if string just contain numeric, false if there's one or more non numeric in string
	 */
	private boolean isNumericWord(String word)
	{
		for (int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) < '2' || word.charAt(i) > '9')
			{
				return false;
			}
		}
		return true;
	}
}
