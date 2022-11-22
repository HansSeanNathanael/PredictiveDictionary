package predictive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is just prototype for predictive dictionary (not efficient algorithm)
 * used to compare with more efficient algorithm
 * @author Hans Sean Nathanael
 *
 */
public class PredictivePrototype {
	
	// this array signatureOfChar used to simplify function wordToSignature
	// The length is 26 representing all the alphabet a-z
	// index 0 is for 'a', index 1 for 'b', and so on until last index (index 25) for 'z'
	private static char signatureOfChar[] = {'2', '2', '2',
											 '3', '3', '3',
											 '4', '4', '4',
											 '5', '5', '5',
											 '6', '6', '6',
											 '7', '7', '7', '7',
											 '8', '8', '8',
											 '9', '9', '9', '9'};
	
	/**
	 * Convert a string of word to a signature (number)
	 * "Hello" become "43556" not "4433555555666"
	 * This algorithm use array signatureOfChar rather than 'nested if'
	 * 'Nested if' make program longer, by this it will easier to read
	 * @param word : String of word that will be converted to its signature
	 * @return signature of converted word
	 */
	public static String wordToSignature(String word)
	{
		// signatureStringBuffer used to make signature of the word and become the return value
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
		// word become lower case for easier conversion
		word = word.toLowerCase(); 
		
		// change every word's characters to it's signature
		for(int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) >= 'a' && word.charAt(i) <= 'z')
			{
				// signatureOfChar will make conversion easier
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
	 * Function to translate signature of word into a word that exist in dictionary file
	 * the algorithm is not efficient because the dictionary not stored in the program
	 * every time this function called, the program will read every line of dictionary
	 * reading file is slow and time consuming, this will slow the program
	 * especially if using Scanner to read every line of file, BufferedReader is
	 * faster than Scanner in this case
	 * @param signature : string of number that will translated to word
	 * @return Set (in form of HashSet) of String that contain all possible word in dictionary
	 */
	public static Set<String> signatureToWords(String signature)
	{
		
		// all char of signature must be numeric
		// if not, then no need to convert it because it can't be converted
		if (isNumericWord(signature) && signature.length() > 0)
		{
			
			// Set containing all possible word match in dictionary
			// HashSet is used because it is fast (O(1) constant time lookup) and
			// use self balance tree for collision, thus worst case is (O(log n) time lookup)
			// HashSet is faster than TreeSet (O(log n) because algorithm of tree) and 
			// LinkedHashSet (similar with HashSet but use LinkedList for collision)
			Set<String> matchedPossibleWord = new HashSet<String>();
			
			try
			{
				// fileReader used to read every line of dictionary file
				// BufferedReader is more efficient than Scanner when reading
				// every line of file, Scanner is a bit slower
				BufferedReader fileReader = new BufferedReader(new FileReader("assets/words"));
				
				// String to store word that have been read from dictionary file
				String fileOneLine = null;
				
				// read all word in dictionary and store it in set if the signature of the
				// words is the same with the signature parameter
				while((fileOneLine = fileReader.readLine()) != null)
				{
					// if text contain non-alphabet, then skipped to next line
					if (isValidWord(fileOneLine))
					{
						// changed to lower case for easier comparison and because
						// all words need to stored in lower case
						fileOneLine = fileOneLine.toLowerCase();
						
						if (wordToSignature(fileOneLine).compareTo(signature) == 0)
						{
							// the word have same signature
							matchedPossibleWord.add(fileOneLine);
						}							
					}
				}
				
				// don't forget to close the BufferedReader
				fileReader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			// returning all word from translated signature that exist in the dictionary
			return matchedPossibleWord;
		}
		
		// return empty set if the signature contain non numerical
		// or the signature is empty
		return new HashSet<String>();
	}
	
	/**
	 * Function to check if a string contain non alphabet
	 * @param word : word to be checked
	 * @return true if all word's characters are alphabet, false if there's one word's character that 
	 *         is non alphabet
	 */
	private static boolean isValidWord(String word)
	{
		for (int i = 0; i < word.length(); i++)
		{
			if ((word.charAt(i) < 'a' || word.charAt(i) > 'z') &&
				(word.charAt(i) < 'A' || word.charAt(i) > 'Z'))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Function to check if a string contain non numerical
	 * @param word : string to be checked
	 * @return true if string just contain numeric, false if there's one or more non numeric in string
	 */
	private static boolean isNumericWord(String word)
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
