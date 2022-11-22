package predictive;

/**
 * This class is used to test DictionaryTreeImpl.signatureToWords() method
 * @author Hans Sean Nathanael
 *
 */
public class Sigs2WordsTree {
	public static void main(String[] args) {
		Dictionary dictionary = new DictionaryTreeImpl("assets/words");
		
		for(String argument : args)
		{
			System.out.println(argument + " : " + dictionary.signatureToWords(argument));
		}
	}
}
