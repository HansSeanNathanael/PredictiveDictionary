package predictive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to store all words in dictionary using map for faster lookup
 * @author Hans Sean Nathanael
 *
 */
public class DictionaryMapImpl implements Dictionary {

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
	
	// mapDictionaryWords used to store all words in dictionary file
	// the key is signature (in String) and the value was Set of words that have same signature
	// HashMap is used because it is the fastest Map implementation (average constant time O(1) for lookup)
	// the reason is because it use hashing to get index of a key and balance tree to store collision
	// which faster than TreeMap (O(Log n) because of self balance tree algorithm) and LinkedListMap (similar 
	// to HashMap but use LinkedList to store collision, slower by a tiny bit)
	// For the value Set it is better to use HashSet because the same reason with HashMap
	private Map<String, Set<String>> mapDictionaryWords = new HashMap<String, Set<String>>();
	
	/**
	 * Constructor of this class
	 * all words in dictionary file will be stored in mapDictionaryWords
	 */
	public DictionaryMapImpl()
	{
		
		try
		{
			// fileReader used to read every line of dictionary file
			// BufferedReader is faster than Scanner when used to read every line of a file one by one
			BufferedReader fileReader = new BufferedReader(new FileReader("assets/words"));
			
			// fileOneLine used to store a line of string that have been read by fileReader
			String fileOneLine = null;
			
			// read every line of dictionary file
			while((fileOneLine = fileReader.readLine()) != null)
			{
				// changed to lower case for easier comparison and because
				// all words need to stored in lower case
				fileOneLine = fileOneLine.toLowerCase();
				
				// word containing non alphabet will be skipped
				if (isValidWord(fileOneLine))
				{
					
					// must checked if the key already exist, if not exist then must
					// create new HashSet for the value (must not forget, if not could
					// lead to null exception), but if the key already exist then just
					// add new word to the HashSet on that mapped key
					String keyFromSignatureOfWord = wordToSignature(fileOneLine);
					if (mapDictionaryWords.containsKey(keyFromSignatureOfWord) == false)
					{
						mapDictionaryWords.put(keyFromSignatureOfWord, new HashSet<String>());
					}
					mapDictionaryWords.get(keyFromSignatureOfWord).add(fileOneLine);
				}
			}
			
			// don't forget to close the BufferedReader
			fileReader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public String wordToSignature(String word)
	{
		// This algorithm use array signatureOfChar rather than 'nested if'
		// 'Nested if' make program longer, by this it will easier to read
		
		// signatureStringBuffer is used to make the signature of the word
		// StringBuffer is used to make signature string rather than using String
		// because it is faster to append a string especially appending many times using loop
		// when append a string using '+', the string converted to StringBuffer and then
		// append the StringBuffer, then convert it back to String, it is faster
		// by just using StringBuffer, no need convert it to String every time it is appended
		StringBuffer signatureStringBuffer = new StringBuffer("");

		// word must no null to be converted
		if (word == null)
		{
			return word;
		}
		word = word.toLowerCase(); // to the word become lower case so it's easier to converted
		
		for(int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) >= 'a' && word.charAt(i) <= 'z')
			{
				signatureStringBuffer.append(signatureOfChar[word.charAt(i)- 'a']);
			}
			else
			{
				signatureStringBuffer.append(" "); // for non alphabet changed to " " (space)
			}
		}
		
		return signatureStringBuffer.toString();
	}
	
	@Override
	public Set<String> signatureToWords(String signature)
	{
		// must check if the signature is value (not containing non numeric characters)
		if (isNumericWord(signature) && mapDictionaryWords.containsKey(signature))
		{
			return mapDictionaryWords.get(signature);
		}
		
		// return empty HashSet if the signature isn't valid (containing non numeric characters)
		// or the key doesn't exist in the dictionary
		// maybe instead like this, it is better to throw exception(?)
		return new HashSet<String>();
	}
	
	/**
	 * Method to check if a string just contain alphabet characters
	 * @param word : string to be checked
	 * @return true if the word just contain alphabet character (lower case), 
	 *         false if it contain non alphabet character (lower case)
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
