package predictive;

/**
 * Class to pair a word with it's signature
 * @author Hans Sean Nathanael
 * 
 */
public class WordSig implements Comparable<WordSig> {
	private String word;
	private String signature;
	
	/**
	 * Constructor with signature of a word and the word
	 * @param signature : signature of the word
	 * @param word : word to be pair with it's signature
	 */
	public WordSig(String signature, String word)
	{
		this.signature = signature;
		this.word = word;
	}
	
	/**
	 * Method get value of word
	 * @return value of word
	 */
	public String getWord()
	{
		return this.word;
	}
	
	/**
	 * Method get value of signature
	 * @return signature of the word
	 */
	public String getSignature()
	{
		return this.signature;
	}

	@Override
	public int compareTo(WordSig o) {
		
		// comparison just by the signature of the word
		// two different word will be count as same if the signature is identical
		// this is because the dictionary doesn't need lookup (search) by the word
		// but bunch of word with same signature, so it doesn't important if the word
		// is not sorted lexicographically
		// and the dictionary doesn't need to find a certain word
		return this.signature.compareTo(o.signature);
	}
	
}
