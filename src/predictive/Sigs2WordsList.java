package predictive;

/**
 * This class is used to test DictionaryListImpl.signatureToWords() method
 * @author Hans Sean Nathanael
 *
 */
public class Sigs2WordsList {

	public static void main(String[] args) {
		DictionaryListImpl dictionary = new DictionaryListImpl();
		
		for(String argument : args)
		{
			System.out.println(argument + " : " + dictionary.signatureToWords(argument));
		}
	}

}
