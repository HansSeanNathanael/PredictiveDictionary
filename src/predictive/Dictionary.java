package predictive;

import java.util.Set;


/**
 * Interface containing method for string dictionary and signature of the words
 * @author Hans Sean Nathanael
 * 
 */
public interface Dictionary {
	
	/**
	 * Method to change word into it's signature
	 * @param word : word to be translated to it's signature
	 * @return string of signature of the words
	 */
	String wordToSignature(String word);
	
	/**
	 * Method to get all words in dictionary with a certain signature
	 * @param signature : signature of the words that will be retrieve
	 * @return set of words that have the same signature
	 */
	Set<String> signatureToWords(String signature);
}
