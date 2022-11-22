package predictive;

/**
 * This class is used to test DictionaryMapImpl.signatureToWords() method
 * @author Hans Sean Nathanael
 *
 */
public class Sigs2WordsMap {
	public static void main(String[] args) {
		Dictionary dictionary = new DictionaryMapImpl();
		
		for(String argument : args)
		{
			System.out.println(argument + " : " + dictionary.signatureToWords(argument));
		}
	}
}
